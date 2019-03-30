package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.UserDao;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.exception.GlobalException;
import com.hjy.miaosha.redis.MiaoshaUserKey;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.utils.MD5Util;
import com.hjy.miaosha.utils.UUIDUtil;
import com.hjy.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Service
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(long id) {
        return userDao.getById(id);
    }

    public Boolean login(HttpServletResponse response,LoginVo loginVo) {

        if (loginVo == null) {
            throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw  new GlobalException(CodeMsg.MOBLIE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass,slatDB);
        if (!calcPass.equals(dbPass)) {
            throw  new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        addCookie(response,user);
        return true;
    }

    public User getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(MiaoshaUserKey.token,token,User.class);
        //延长有效区
        if (user!=null) {
            addCookie(response, user);
        }
        return user;
    }


    private void addCookie(HttpServletResponse response,User user) {
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        //设置到根目录
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
