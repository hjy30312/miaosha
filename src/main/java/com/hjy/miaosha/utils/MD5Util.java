package com.hjy.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static  String md5(String str) {
        return DigestUtils.md5Hex(str);
   }

   private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass) {
        String str = " " + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDbPass(String input, String saltDB) {
        String formPass = inputPassToFormPass(input);
        String dbPass = formPassToDBPass(formPass,saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
    }

}
