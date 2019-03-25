package com.zrk.bsblog.controller;

import com.zrk.bsblog.domain.Authority;
import com.zrk.bsblog.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.zrk.bsblog.service.AuthorityService;
import com.zrk.bsblog.service.UserService;
import com.zrk.bsblog.util.ConstraintViolationExceptionHandler;
import com.zrk.bsblog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.ConstraintViolationException;

/**
 * Hello World 控制器.
 */
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService authorityService;

	//查询所有用户
    @GetMapping
	private ModelAndView list(@RequestParam(value = "async",required = false) boolean async,
                              @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                              @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
							  @RequestParam(value = "name", required = false, defaultValue = "")String name,
                              Model model){

        Pageable pageable=PageRequest.of(pageIndex,pageSize);
		Page<User> page = userService.listUsersByNameLike(name,pageable);
		List<User> list=page.getContent();//当前所在页面数据列表

        model.addAttribute("page",page);
        model.addAttribute("userList",list);
		return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list", "userModel", model);
	}

	//获取form表单页面
	@GetMapping("/add")
	public ModelAndView createForm(Model model) {
		model.addAttribute("user",new User(null,null,null,null));

		return new ModelAndView("users/add","userModel",model);
	}

	//新建用户或修改对象
	@PostMapping
	public ResponseEntity<Response> create(User user, Long authorityId){
    	List<Authority> authorities=new ArrayList<>();
    	authorities.add(authorityService.getAuthorityById(authorityId));
    	user.setAuthorities(authorities);

    	if (user.getId()==null){
    		user.setEncodePassword(user.getPassword());
		}else {
			//判断密码是否做了变更
			User originalUser = userService.getUserById(user.getId());
			String rawPassword = originalUser.getPassword();
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodePasswd = encoder.encode(user.getPassword());
			System.out.println(encodePasswd);
			boolean isMatch = encoder.matches(rawPassword, encodePasswd);
			if (!isMatch) {
				user.setEncodePassword(user.getPassword());
			} else {
				user.setPassword(user.getPassword());
			}
		}
			try{
			userService.saveOrUpateUser(user);
			}catch (ConstraintViolationException e) {
				return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
			}
		return ResponseEntity.ok().body(new Response(true, "处理成功", user));
	}

	//删除用户
	@RequestMapping(value = "delete/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
		try {
			userService.removeUser(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}

	//修改用户
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id")Long id,Model model){
		User user = userService.getUserById(id);
		model.addAttribute("user",user);
		return new ModelAndView("users/edit","userModel",model);
	}
}
 