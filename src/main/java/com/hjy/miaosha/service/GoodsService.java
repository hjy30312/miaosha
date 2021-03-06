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

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int flag = goodsDao.reduceStock(g);
        return flag > 0;
    }

    public boolean insert(Goods goods) {
        int flag = goodsDao.insert(goods);
        return flag > 0;
    }

    public Boolean deleteById(Long goodsId) {
        int flag = goodsDao.deleteGoodsById(goodsId);
        return flag > 0;
    }

    public List<GoodsVo> listGoodsVoByName(String name) {
        String key = new StringBuilder().append("%").append(name).append("%").toString();
        return goodsDao.listGoodsVoByName(key);
    }
}
