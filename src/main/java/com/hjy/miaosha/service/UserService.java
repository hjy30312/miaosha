package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.UserDao;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.exception.GlobalException;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.utils.MD5Util;
import com.hjy.miaosha.utils.UUIDUtil;
import com.hjy.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin.util.UIUtil;


@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RedisService redisService;


    public User getById(long id) {
        return userDao.getById(id);
    }

    public Boolean login(LoginVo loginVo) {
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

        return true;
    }


}
