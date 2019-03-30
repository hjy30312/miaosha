package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    UserService userService;


    @RequestMapping("/to_list")
    public String toLogin(Model model,
                          User user) {
        model.addAttribute("user",user);
        return "goods_list";
    }


}
