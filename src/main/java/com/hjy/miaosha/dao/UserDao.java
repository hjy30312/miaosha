package com.hjy.miaosha.dao;


import com.hjy.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from miaosha_user where id = #{id}")
    public User getById(@Param("id") Long id);

}
