package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.MiaoshaGoodsDao;
import com.hjy.miaosha.domain.MiaoshaGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiaoshaGoodsService {


    @Autowired
    MiaoshaGoodsDao miaoshaGoodsDao;


    public List<MiaoshaGoods> list() {
        return miaoshaGoodsDao.getList();
    }


    public boolean insert(MiaoshaGoods goods) {
        int flag = miaoshaGoodsDao.insert(goods);
        return flag > 0;
    }

    public boolean deleteById(long id) {
        int flag = miaoshaGoodsDao.deleteById(id);
        return flag > 0;
    }
}
