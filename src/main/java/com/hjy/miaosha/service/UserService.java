package com.hjy.miaosha.service;

import com.aliyuncs.exceptions.ClientException;
import com.hjy.miaosha.dao.UserDao;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.exception.GlobalException;
import com.hjy.miaosha.redis.MiaoshaUserKey;
import com.hjy.miaosha.redis.RedisService;
import com.hjy.miaosha.redis.UserKey;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.utils.MD5Util;
import com.hjy.miaosha.utils.SendMessageUtil;
import com.hjy.miaosha.utils.UUIDUtil;
import com.hjy.miaosha.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;

    public User getById(long id) {
        //取缓存
        User user = redisService.get(MiaoshaUserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userDao.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    /**
     * 先更新数据库 再删除相对应缓存
     *
     * @param id
     * @param formPass
     * @return
     */
    public boolean updatePassword(long id, String formPass) {
        //取user
        User user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBLIE_NOT_EXIST);
        }
        //更新数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userDao.update(toBeUpdate);
        //处理缓存  删除
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        return true;
    }


    public User login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBLIE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, slatDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return user;
    }

    /**
     *  1.设置redis缓存，tk+token为key，用户信息为value， 过期时间为2天
     *  2.设置用户浏览器Cookie，"token"为key，token为value。
     * @param response 服务器的响应
     * @param token 生成的随机数
     * @param user 用户信息
     */
    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        //设置到根目录
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 从redis中获取用户信息
     */
    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(MiaoshaUserKey.token, token, User.class);
        //延长有效区
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }



    @Transactional
    public String register(LoginVo user) {
        if (user == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = user.getMobile();

        //判断手机号是否存在
        User user1 = userDao.getById(Long.parseLong(mobile));
        if (user1 != null) {
            throw new GlobalException(CodeMsg.MOBLIE_EXIST);
        }
        User newUser = new User();
        newUser.setId(Long.parseLong(user.getMobile()));
        newUser.setPassword(MD5Util.formPassToDBPass(user.getPassword(), "1a2b3c4d"));
        newUser.setSalt("1a2b3c4d");
        newUser.setNickname(user + user.getMobile());
        newUser.setRegisterDate(new Date());
        newUser.setLastLoginDate(new Date());
        int resultCount = userDao.insert(newUser);
        if (resultCount == 0) {
            return "注册失败";
        }
        return "注册成功";
    }


    /**
     * 检查验证码 通过手机号去缓存数据库中找相对应的
     * 验证码，返回比较情况
     *
     * @param mobile       手机号
     * @param identifyCode 验证码
     * @return
     */
    public boolean checkIsCorrectCode(long mobile, String identifyCode) {
        String redisData = redisService.get(UserKey.getMessage, "" + mobile, String.class);
        if (redisData.equals(identifyCode)) {
            return true;
        }
        return false;
    }

    /**
     * 发送验证码  1.生成随机数 2.存入redis 3.发送
     *
     * @param mobile
     * @return
     */
    public boolean sendMessage(long mobile) throws ClientException {
        String key = String.valueOf(mobile);
        String random = UUIDUtil.getRandom(6);
        redisService.set(UserKey.getMessage, key, random);
        SendMessageUtil.sendSms(key, random);
        return true;
    }


    public List<User> getList() {
        return  userDao.getList();

    }
}
