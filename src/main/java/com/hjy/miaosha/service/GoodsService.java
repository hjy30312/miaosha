package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.GoodsDao;
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

}
