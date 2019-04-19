package com.hjy.miaosha.dao;


import com.hjy.miaosha.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    @Update("update miaossha_user set password = #{password} where id = #{id}")
    void update(User toBeUpdate);

    @Insert("insert into miaosha_user(id,nickname,password,salt,register_date,last_login_date,login_count)"
            + " value(#{id},#{nickname},#{password},#{salt},#{registerDate},#{lastLoginDate},#{loginCount})")
    int insert(User user);

    @Select("select * from miaosha_user where id = #{id}")
    User getById(@Param("id") Long id);

}
