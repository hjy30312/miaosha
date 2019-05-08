package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.Goods;
import com.hjy.miaosha.domain.MiaoshaGoods;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.redis.GoodsKey;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.UserService;
import com.hjy.miaosha.vo.GoodsDetailVo;
import com.hjy.miaosha.vo.GoodsVo;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
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

    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,
                       Model model, User user) {
        model.addAttribute("user",user);
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 查询商品列表
        List<GoodsVo> goodVoList =  goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodVoList);
         // return "goods_list";
        IWebContext ctx = new WebContext(request,response,  request.getServletContext(),request.getLocale(), model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("index1",ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "" , html);
        }
        return html;
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

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> insert(Goods goods) {
        boolean flag = goodsService.insert(goods);
        if (flag) {
            return Result.success(CodeMsg.GOODS_INSETRT_SUCCESS.getMsg());
        }
        return Result.error(CodeMsg.GOODS_INSESRT_ERROR);
    }

    @RequestMapping(value = "miaosha/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> miaoshaInsert(MiaoshaGoods goods) {
        boolean flag = goodsService.miaoshaInsert(goods);
        if (flag) {
            return Result.success(CodeMsg.MIAOSHA_GOODS_INSETRT_SUCCESS.getMsg());
        }
        return Result.error(CodeMsg.MIAOSHA_GOODS_INSESRT_ERROR);
    }
}
