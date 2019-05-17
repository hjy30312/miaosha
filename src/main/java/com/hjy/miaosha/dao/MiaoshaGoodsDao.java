package com.hjy.miaosha.dao;

import com.hjy.miaosha.domain.MiaoshaGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MiaoshaGoodsDao {


    @Select("select * from miaosha_goods")
    List<MiaoshaGoods> getList();
}
