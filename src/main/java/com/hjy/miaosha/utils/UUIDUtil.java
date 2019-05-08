package com.hjy.miaosha.utils;


import java.util.*;

public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 获取n位 的随机位数数字
     * @return result
     */
    public static String getRandom(int n) {
        String result = "";
        Random rand = new Random();
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);

            result += randInt;
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        // 1.
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if ("1".equals(item)) {
                iterator.remove();
            }
        }
        // 2.
        for (String item : list) {
            if ("2".equals(item)) {
                list.remove(item);
            }
        }
    }
}
