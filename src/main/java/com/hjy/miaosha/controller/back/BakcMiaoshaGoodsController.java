package com.hjy.miaosha.controller.back;

import com.hjy.miaosha.domain.MiaoshaGoods;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.MiaoshaGoodsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> insert(MiaoshaGoods goods) {
        log.info(goods.toString());
        boolean flag = miaoshaGoodsService.insert(goods);
        if (flag) {
            return Result.success(CodeMsg.MIAOSHA_GOODS_INSETRT_SUCCESS.getMsg());
        }
        return Result.error(CodeMsg.MIAOSHA_GOODS_INSESRT_ERROR);
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Result<String> delete(@RequestParam("id")long id) {
        boolean flag = miaoshaGoodsService.deleteById(id);
        if (flag) {
            return Result.success(CodeMsg.DELETE_SUCCESS.getMsg());
        }
        return Result.error(CodeMsg.DELETE_ERROR);
    }
}