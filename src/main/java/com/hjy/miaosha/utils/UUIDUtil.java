package com.hjy.miaosha.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
    /**
     * 获取6位 的随机位数数字
     * @return result
     */
    public static String getRandom6() {
        String result = "";
        Random rand = new Random();
        int n = 6;
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);

            result += randInt;
        }
        return result;
    }
}
