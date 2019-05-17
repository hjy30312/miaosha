package com.hjy.miaosha.controller.back;

import com.hjy.miaosha.domain.MiaoshaGoods;
import com.hjy.miaosha.domain.OrderInfo;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.service.MiaoshaGoodsService;
import com.hjy.miaosha.service.MiaoshaService;
import com.hjy.miaosha.service.OrderService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log
@RequestMapping("/back/miaosha_goods")
public class BakcMiaoshaGoodsController {

    @Autowired
    MiaoshaGoodsService miaoshaGoodsService;

    @RequestMapping(value = "/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        //查询用户列表
        List<MiaoshaGoods> miaoshaGoodsList = miaoshaGoodsService.list();
        model.addAttribute("miaoshaGoodsList", miaoshaGoodsList);
        return "back_miaosha_goods_list";
    }

    @RequestMapping(value = "/to_insert")
    public String toInsert(Model model, User user) {
        model.addAttribute("user", user);
        return "back_miaosha_goods_insert";
    }


}