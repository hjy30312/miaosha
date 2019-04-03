package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.GoodsDao;
import com.hjy.miaosha.domain.Goods;
import com.hjy.miaosha.domain.MiaoshaGoods;
import com.hjy.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;


    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();

    }

    /**
     * 通过商品ID 得到相对于商品详情
     * @param goodsId
     * @return
     */
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.geGoodsVoByGoodsId(goodsId);

    }

    public void reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        goodsDao.reduceStock(g);
    }
}
