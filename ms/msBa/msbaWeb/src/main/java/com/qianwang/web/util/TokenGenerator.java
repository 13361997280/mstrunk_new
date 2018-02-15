package com.qianwang.web.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by zhoufengbo on 2015/10/14.
 */
public class TokenGenerator {
    private TokenGenerator(){}
    private static TokenGenerator instance = new TokenGenerator();
    public static TokenGenerator getInstance(){
        return instance;
    }
    public String generateTokeCode(){
        String value = System.currentTimeMillis()+new Random().nextInt()+"";
        //获取数据指纹，指纹是唯一的
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] b = md.digest(value.getBytes());//产生数据的指纹
            //Base64编码
            BASE64Encoder be = new BASE64Encoder();
            be.encode(b);
            return be.encode(b);//制定一个编码
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
