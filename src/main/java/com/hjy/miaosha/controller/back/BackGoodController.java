package com.hjy.miaosha.controller.back;

import com.hjy.miaosha.domain.Goods;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.GoodsVo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.List;

@Controller
@RequestMapping("/back/goods")
@Log
public class BackGoodController {

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    RedisService redisService;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        // 查询商品列表
        List<GoodsVo> goodVoList =  goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodVoList);
        return "back_goods_list";
    }

    @RequestMapping(value = "/to_insert")
    public String toInsert(Model model, User user) {
        model.addAttribute("user", user);
        return "back_goods_insert";
    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> insert(Goods goods) {
        boolean flag = goodsService.insert(goods);
        if (flag) {
            return Result.success(CodeMsg.GOODS_INSETRT_SUCCESS.getMsg());
        }
        return Result.error(CodeMsg.GOODS_INSESRT_ERROR);
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> deleteGoodsById(@PathVariable("goodsId")Long goodsId){
        return Result.success(goodsService.deleteById(goodsId));
    }
}
