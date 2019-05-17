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


}
