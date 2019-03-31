package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodService {

    @Autowired
    GoodsDao goodsDao;

}
