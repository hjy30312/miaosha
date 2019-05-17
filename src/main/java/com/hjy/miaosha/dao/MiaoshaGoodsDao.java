package com.hjy.miaosha.dao;

import com.hjy.miaosha.domain.MiaoshaGoods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MiaoshaGoodsDao {


    @Select("select * from miaosha_goods")
    List<MiaoshaGoods> getList();

    @Insert("insert into miaosha_goods(goods_id, miaosha_price,stock_count,start_date,end_date)values("
            + "#{goodsId},#{miaoshaPrice},#{stockCount},#{startDate},#{endDate})")
    int insert(MiaoshaGoods goods);

    @Delete("delete from miaosha_goods where id = #{id}")
    int deleteById(@Param("id")long id);
}
