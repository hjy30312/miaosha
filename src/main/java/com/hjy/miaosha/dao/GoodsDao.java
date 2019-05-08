package com.hjy.miaosha.dao;


import com.hjy.miaosha.domain.Goods;
import com.hjy.miaosha.domain.MiaoshaGoods;
import com.hjy.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId} limit 1")
    GoodsVo geGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(MiaoshaGoods g);

    @Insert("insert into goods(goods_name, goods_title, goods_img, goods_detail, goods_price, goods_stock)values("
            + "#{goodsName}, #{goodsTitle}, #{goodsImg},#{goodsDetail} ,#{goodsPrice},#{goodsStock})")
    int insert(Goods goods);

    @Insert("insert into miaosha_goods(goods_id, miaosha_price,stock_count,start_date,end_date)values("
            + "#{goodsId},#{miaoshaPrice},#{stockCount},#{startDate},#{endDate})")
    int miaoshaInsert(MiaoshaGoods goods);
}