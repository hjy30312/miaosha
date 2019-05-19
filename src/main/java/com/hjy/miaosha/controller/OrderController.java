package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.OrderInfo;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.OrderService;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.GoodsVo;
import com.hjy.miaosha.vo.OrderDetailVo;
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
@RequestMapping("/order")
@Log
public class OrderController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Result<OrderDetailVo> info(User user, @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }



    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> delete(@RequestParam("orderId") long orderId) {
        return Result.success(orderService.deleteOrderById(orderId));
    }

    @RequestMapping(value = "/list_by_id", method = RequestMethod.POST)
    public String list(User user, Model model) {
        if (user == null) {
            //return "login";
            return "user_index";
        }
        //List<OrderInfo>  orderInfos = orderService.getOrderListByUserId(user.getId());

        List<OrderInfo>  orderInfos = orderService.getOrderListByUserId(Long.parseLong("18772842517"));
        model.addAttribute("orderInfos",orderInfos);
        for (OrderInfo orderInfo:orderInfos) {
            log.info(orderInfo.toString());
        }
        return "user_index";
    }

}
