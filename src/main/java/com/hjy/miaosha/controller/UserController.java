package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;



    @RequestMapping("/info")
    @ResponseBody
    public Result<User> toLogin(Model model,
                          User user) {
        return Result.success(user);
    }
}
