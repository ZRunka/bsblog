package com.zrk.bsblog.controller;

import com.zrk.bsblog.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminController {

    //获取后台管理住页面
    @GetMapping
    public ModelAndView listUsers(Model model){
        List<Menu> list=new ArrayList<>();
        list.add(new Menu("用户管理","/users"));
        list.add(new Menu("博客管理", "/blogs"));
        model.addAttribute("list",list);
        return new ModelAndView("/admins/index","model",model);
    }
}
