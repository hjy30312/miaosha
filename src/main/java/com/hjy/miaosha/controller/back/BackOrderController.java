package com.hjy.miaosha.controller.back;

import com.hjy.miaosha.domain.OrderInfo;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.service.OrderService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Log
@RequestMapping("/back/order")
public class BackOrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/to_list")
    public String list(Model model, User user) {
        model.addAttribute("user", user);
        //查询用户列表
        List<OrderInfo> ordersList = orderService.getOrderList();
        model.addAttribute("ordersList", ordersList);
        return "back_order_list";
    }

}
