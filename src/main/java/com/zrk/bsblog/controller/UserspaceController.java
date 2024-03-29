package com.zrk.bsblog.controller;

import com.zrk.bsblog.domain.Blog;
import com.zrk.bsblog.domain.Catalog;
import com.zrk.bsblog.domain.User;
import com.zrk.bsblog.domain.Vote;
import com.zrk.bsblog.service.BlogService;
import com.zrk.bsblog.service.CatalogService;
import com.zrk.bsblog.service.UserService;
import com.zrk.bsblog.util.ConstraintViolationExceptionHandler;
import com.zrk.bsblog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username")String username,Model model){
        User user=(User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username")String username, Model model){
        User user=(User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("userspace/profile","userModel",model);
    }

    //保存个人设置
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username")String username,User user) {
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        //判断密码是否做了变更
        String rawPassword=originalUser.getPassword();
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodePasswd=encoder.encode(user.getPassword());
        boolean isMatch=encoder.matches(rawPassword,encodePasswd);
        if (!isMatch){
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpateUser(originalUser);
        return "redirect:/u/" + username +"/profile";
    }

    //获取编辑头像的界面
    @GetMapping("/{username}/avater")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username")String username,Model model){
        User user=(User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("userspace/avatar","userModel",model);
    }

    //保存头像
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username")String username,@RequestBody User user){
        String avatarUrl=user.getAvatar();

        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpateUser(originalUser);

        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }

    //获取用户的博客列表
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value="order",required=false,defaultValue="new") String order,
                                   @RequestParam(value="catalog",required=false ) Long catalogId,
                                   @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
                                   @RequestParam(value="async",required=false) boolean async,
                                   @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                   @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                                   Model model){

        User  user = (User)userDetailsService.loadUserByUsername(username);

        Page<Blog> page = null;

        if (catalogId!=null && catalogId>0){//分类查询
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = PageRequest.of(pageIndex,pageSize);
            page = blogService.listBlogsByCatalog(catalog,pageable);
            order = "";
        }else if (order.equals("hot")){//分类查询
            Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable=PageRequest.of(pageIndex,pageSize,sort);
            page=blogService.listBlogsByTitleVoteAndSort(user,keyword,pageable);
        }else if (order.equals("new")){
            Pageable pageable=PageRequest.of(pageIndex,pageSize);
            page=blogService.listBlogsByTitleVote(user,keyword,pageable);
        }
        List<Blog> list=page.getContent();//当前所在页面数据列表
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async==true ? "/userspace/u :: #mainContainerRepleace":"/userspace/u");
    }

    //获取博客展示页面
    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("username")String username,@PathVariable("id")Long id,Model model){
        User principal = null;
        Blog blog = blogService.getBlogById(id);

        //每次读取，简单的可以认为阅读量+1
        blogService.readingIncrease(id);

        boolean isBlogOwner=false;

        //判断操作用户是否是博客的所有者
        if(SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            principal=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())){
                isBlogOwner=true;
            }
        }

        //判断操作用户的点赞情况
        List<Vote> votes = blog.getVotes();
        Vote currentVote = null;//当前用户的点赞情况

        if (principal !=null){
            for (Vote vote : votes){
                vote.getUser().getUsername().equals(principal.getUsername());
                currentVote = vote;
                break;
            }
        }
        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel",blog);
        model.addAttribute("currentVote",currentVote);

        return "/userspace/blog";
    }

    @DeleteMapping("/{username}/blogs/{id}")
//    @RequestMapping(value = "delete/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username")String username,@PathVariable("id")Long id){

        try{
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl="/u/"+username+"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    //获取新增博客界面
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username")String username, Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("catalogs",catalogs);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    //获取编辑blog的界面
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username")String username,@PathVariable("id")Long id,Model model){
        //获取用户分类列表
        User user = (User)userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        model.addAttribute("blog",blogService.getBlogById(id));
        model.addAttribute("catalogs",catalogs);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    //保存博客
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username")String username,@RequestBody Blog blog){
        //对catalog进行空处理
        if (blog.getCatalog().getId() == null){
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {
            //判断是修改还是新增
            if (blog.getId() !=null){
                Blog orignalBlog = blogService.getBlogById(blog.getId());
                orignalBlog.setTitle(blog.getTitle());
                orignalBlog.setContent(blog.getContent());
                orignalBlog.setSummary(blog.getSummary());
                orignalBlog.setCatalog(blog.getCatalog());
                orignalBlog.setTags(blog.getTags());
                blogService.saveBlog(orignalBlog);
            }else{
                User user = (User)userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }
}

