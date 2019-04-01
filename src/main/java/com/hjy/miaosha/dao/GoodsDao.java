package com.hjy.miaosha.dao;

import com.hjy.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on my.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

}