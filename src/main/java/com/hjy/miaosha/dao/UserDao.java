package com.hjy.miaosha.dao;


import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.vo.LoginVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Insert("insert into miaosha_user(id,nickname,password,salt,register_date,last_login_date,login_count)"
            + " value(#{id},#{nickname},#{password},#{salt},#{registerDate},#{lastLoginDate},#{loginCount})")
    int insert(User user);

    @Select("select * from miaosha_user where id = #{id}")
    User getById(@Param("id") Long id);

}
