package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.GoodsDetailVo;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
@Log
public class GoodController {

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

    /**
     * 显示商品列表时
     * 1。先从缓存中获取
     * 2。如果没有则从数据库中获取并放入缓存
     * 3。数据渲染html
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       Model model, User user) {
        model.addAttribute("user",user);
        // 查询商品列表
        List<GoodsVo> goodVoList =  goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodVoList);
        return "index1";
    }

    @RequestMapping(value = "/to_search_list")
    public String search(Model model, User user, String name) {
        model.addAttribute("user", user);
        // 查询商品列表
        List<GoodsVo> goodVoList =  goodsService.listGoodsVoByName(name);
        model.addAttribute("goodsList", goodVoList);
        return "goods_list";
    }






    /**
     * 页面静态化
     * @return
     */
    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(User user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        //状态
        int miaoshaStatus;
        //具体开始时间
        int remainSeconds;
        if (now < startAt) {
            //秒杀还没开始,倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now)/1000);
        } else if (now > endAt){
            //秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo detailVo = new GoodsDetailVo();
        detailVo.setGoods(goods);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(detailVo);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> deleteGoodsById(@PathVariable("goodsId")Long goodsId){
        return Result.success(goodsService.deleteById(goodsId));
    }




}
