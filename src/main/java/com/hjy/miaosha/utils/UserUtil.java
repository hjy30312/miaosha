package com.hjy.miaosha.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hjy.miaosha.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<User>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
                User user = new User();
                user.setId(13000000000L+i);
                user.setLoginCount(1);
                user.setNickname("user" + i);
                user.setSalt("1a2b3c");
                user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
                users.add(user);
        }
        System.out.println("create user");
        //插入数据库  //todo
        Connection conn = DBUtil.getConn();
        String sql = "insert into miaosha_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            preparedStatement.setInt(1, user.getLoginCount());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setString(4, user.getSalt());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        conn.close();
        System.out.println("insert to db");
        //登录， 生成token
        String urlString = "http://localhost:8080/login/do_login";
        File file = new File("C:tokens.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile randomAccessFile  = new RandomAccessFile(file, "rw");
        file.createNewFile();
        randomAccessFile.seek(0);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId()
                    + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token ：" + user.getId());


        }

    }


}
