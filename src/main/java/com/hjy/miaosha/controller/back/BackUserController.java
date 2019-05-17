package com.hjy.miaosha.controller.back;

import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/back/user")
@Log
public class BackUserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        //查询用户列表
        List<User> usersList = userService.getList();
        model.addAttribute("usersList", usersList);
        return "back_user_list";
    }

}

