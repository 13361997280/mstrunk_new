package com.qianwang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

	public static final String MD5="MD5";

    /**
     * 采用加密算法加密字符串数据
     * @param str   需要加密的数据
     * @param algorithm 采用的加密算法
     * @return 字节数据
     * @throws NoSuchAlgorithmException 
     */
    public static byte[] encryptionStrBytes(String str, String algorithm) throws NoSuchAlgorithmException {
        // 加密之后所得字节数组
        byte[] bytes = null;
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //添加要进行计算摘要的信息
            md.update(str.getBytes());
            //得到该摘要
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
           throw e;
        }
        return null==bytes?null:bytes;
    }



    /**
     * 把字节数组转化成字符串返回
     * @param bytes
     * @return
     */
    public static String bytesConvertToHexString(byte [] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
          String s=Integer.toHexString(0xff & aByte);
            if(s.length()==1){
                sb.append("0"+s);
            }else{
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * 采用加密算法加密字符串数据
     * @param str   需要加密的数据
     * @param algorithm 采用的加密算法
     * @return 字节数据
     * @throws NoSuchAlgorithmException 
     */
    public static String encryptionStr(String str, String algorithm) throws NoSuchAlgorithmException {
        // 加密之后所得字节数组
        byte[] bytes = encryptionStrBytes(str,algorithm);
        return bytesConvertToHexString(bytes);
    }
    
    public static String encryptionStr(String str) throws NoSuchAlgorithmException {
    	return encryptionStr(str, MD5);
    }
}
