package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.LoginVo;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 跳转注册界面
     * @return
     */
    @RequestMapping("/to_register")
    public String toRegister() {
        return "register";
    }

    /**
     * 做登录
     * @param user
     * @return
     */
    @RequestMapping("do_register")
    @ResponseBody
    public Result<String> doLogin(@Valid LoginVo user) {
        logger.warn(String.valueOf(user));
        return Result.success(userService.register(user));
    }


    /**
     * 跳转登录界面
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    /**
     * 做登录
     * @return
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        String token =  userService.login(response,loginVo);
        return Result.success(token);
    }

    /**
     * 显示用户信息
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(Model model,
                                User user) {
        return Result.success(user);
    }
}
