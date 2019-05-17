package com.hjy.miaosha.controller;


import com.aliyuncs.exceptions.ClientException;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.MiaoshaUserKey;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.LoginVo;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/user/")
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
    @RequestMapping("to_register")
    public String toRegister() {
        return "register";
    }

    /**
     * 做注册
     * @param user
     * @return
     */
    @RequestMapping("do_register")
    @ResponseBody
    public Result<String> doRegister(@Valid LoginVo user) {
        return Result.success(userService.register(user));
    }

    /**
     * 做注册带手机验证码
     * @param user
     * @return
     */
    @RequestMapping("do_register2")
    @ResponseBody
    public Result<String> doRegister2(@Valid LoginVo user, String identifyCode) {
        Boolean flag = userService.checkIsCorrectCode(Long.parseLong(user.getMobile()),identifyCode);
        if (!flag) {
            return Result.error(CodeMsg.VERIFYCODE_ERROR);
        }
        return Result.success(userService.register(user));
    }

    /**
     * 跳转登录界面
     * @return
     */
    @RequestMapping("to_login")
    public String toLogin(){
        return "login";
    }

    /**
     * 做登录
     * @return
     */
    @RequestMapping("do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        String token =  userService.login(response,loginVo);
        return Result.success(token);
    }

    /**
     * 显示用户信息
     * @param user
     * @return
     */
    @RequestMapping("info")
    @ResponseBody
    public Result<User> info(User user) {
        return Result.success(user);
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @RequestMapping("update")
    public String updatePassword(User user) {
        if (user == null) {
            return "login";
        }
        userService.updatePassword(user.getId(),user.getPassword());
        return "operate_success";
    }

    /**
     * 登出 注销登录
     * @param user
     * @return
     */
    @RequestMapping("logout")
    public String logout(HttpServletRequest request, User user){
        if (user == null) {
            return "login";
        }
        request.removeAttribute(UserService.COOKI_NAME_TOKEN);
        redisService.delete(MiaoshaUserKey.getById,""+user.getId());
        return "operate_success";
    }

    /**
     * 向手机号发送验证码
     * @param mobile
     * @return
     */
    @RequestMapping("send_message")
    @ResponseBody
    public Result<String> sendVerificationCode(long mobile){
        try {
            userService.sendMessage(mobile);
        } catch (ClientException e) {
            return Result.error(CodeMsg.MOBLIE_CHECK_ERROR);
        }
        return Result.success("发送成功");
    }
}
