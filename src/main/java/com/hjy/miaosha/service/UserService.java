package com.hjy.miaosha.service;

import com.hjy.miaosha.dao.UserDao;
import com.hjy.miaosha.domain.User;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.utils.MD5Util;
import com.hjy.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    UserDao userDao;


    public User getById(long id) {
        return userDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo) {
        if (loginVo == null) {
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            return CodeMsg.MOBLIE_NOT_EXIST;
        }
        //验证密码
        String dbPass = user.getPassword();
        String slatDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass,slatDB);
        if (!calcPass.equals(dbPass)) {
            return CodeMsg.PASSWORD_ERROR;
        }

        return CodeMsg.SUCCESS;
    }


}
