package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.MiaoshaUserKey;
import com.hjy.miaosha.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    UserService userService;


    @RequestMapping("/to_list")
    public String toLogin(Model model,
                          @CookieValue(value = UserService.COOKI_NAME_TOKEN) String cookieToken) {

        if (StringUtils.isEmpty(cookieToken)) {
            return "login";
        }
        String token = cookieToken;
        User user = userService.getByToken(token);
        model.addAttribute("user",user);
        return "goods_list";
    }


}
