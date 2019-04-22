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
import java.util.Date;


@Service
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(long id) {
        //取缓存
        User user = redisService.get(MiaoshaUserKey.getById, ""+id, User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userDao.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, ""+id, User.class);
        }
        return user;
    }

    /**
     * 先更新数据库 再删除相对应缓存
     * @param id
     * @param formPass
     * @return
     */
    public boolean updatePassword(long id, String formPass) {
        //取user
        User  user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBLIE_NOT_EXIST);
        }
        //更新数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userDao.update(toBeUpdate);
        //处理缓存  删除
        redisService.delete(MiaoshaUserKey.getById,""+id);
        return true;
    }

    /**
     * 清空缓存
     * @param id
     */
    public void logout(long id) {
        redisService.delete(MiaoshaUserKey.getById,""+id);
    }


    public String login(HttpServletResponse response,LoginVo loginVo) {

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
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return token;
    }

    public User getByToken(HttpServletResponse response,String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(MiaoshaUserKey.token,token,User.class);
        //延长有效区
        if (user!=null) {
            addCookie(response, token, user);
        }
        return user;
    }


    private void addCookie(HttpServletResponse response,String token,User user) {
        redisService.set(MiaoshaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        //设置到根目录
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String register(LoginVo user) {
        if (user == null) {
            throw  new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = user.getMobile();

        //判断手机号是否存在
        User user1 = userDao.getById(Long.parseLong(mobile));
        if (user1 != null) {
            throw  new GlobalException(CodeMsg.MOBLIE_EXIST);
        }

        User newUser = new User();
        newUser.setId(Long.parseLong(user.getMobile()));
        newUser.setPassword(MD5Util.formPassToDBPass(user.getPassword(),"1a2b3c4d"));
        newUser.setNickname(user + user.getMobile());
        newUser.setRegisterDate(new Date());
        newUser.setLastLoginDate(new Date());
        int resultCount = userDao.insert(newUser);
        if (resultCount == 0) {
            return "注册失败";
        }
        return "注册成功";
    }
}
