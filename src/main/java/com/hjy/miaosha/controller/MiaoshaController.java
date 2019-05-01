package com.hjy.miaosha.controller;

import com.hjy.miaosha.domain.MiaoshaOrder;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.rabbitmq.MQSender;
import com.hjy.miaosha.rabbitmq.MiaoshaMessage;
import com.hjy.miaosha.redis.GoodsKey;
import com.hjy.miaosha.redis.MiaoshaKey;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import com.hjy.miaosha.service.GoodsService;
import com.hjy.miaosha.service.MiaoshaService;
import com.hjy.miaosha.service.OrderService;
import com.hjy.miaosha.utils.MD5Util;
import com.hjy.miaosha.utils.UUIDUtil;
import com.hjy.miaosha.vo.GoodsVo;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.imageio.ImageIO;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {


    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    MQSender sender;
    /**
     * 用于本地判断库存，减少对redis的访问
     */
    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }



    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(User user
            ,@RequestParam("goodsId")long goodsId
            ,@RequestParam(value="verifyCode", defaultValue="0")int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            //验证码错误
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user,goodsId);
        return Result.success(path);
    }



    /**
     * 判断秒杀结果
     * orderId: 成功
     * -1： 秒杀失败
     * 0：排队中
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, User user,
                                      @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);

        return Result.success(result);
    }


    /**
     * 做秒杀操作
     * GET POST区别：
     * GET幂等  从服务端获取数据 无论调用多少次 产生结果都是一样的不会对服务端数据产生任何影响
     * POST   向服务器端提交数据 例如：对服务器端发生变化
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, User user
                ,@RequestParam("goodsId") long goodsId
                ,@PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoshaService.checkPath(user.getId(),goodsId,path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean overFlag = localOverMap.get(goodsId);
        if (overFlag) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);   //标记已经没有库存了
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        // 入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setGoodsId(goodsId);
        message.setUser(user);
        //放入消息队列   在里面进行有关数据库的操作
        sender.sendMiaoshaMessage(message);
        return Result.success(0);
    }



    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, User user,
                                               @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }



}
