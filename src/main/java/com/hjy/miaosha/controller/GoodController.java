package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.Goods;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;


    @RequestMapping("/to_list")
    public String toLogin(Model model,
                          User user) {
        model.addAttribute("user",user);
        //查询商品列表
        List<GoodsVo> goodVoList =  goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodVoList);
        return "goods_list";
    }


}
