package com.hjy.miaosha.controller;


import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;


    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }



    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","hjy");
        return "hello";
    }


    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        //User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        //return Result.success(user);
        return null;
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
       // user.setId(1);
        //user.setName("1111");
       // redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }


}
