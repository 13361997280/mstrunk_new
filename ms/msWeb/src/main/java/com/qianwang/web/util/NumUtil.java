package com.qianwang.web.util;


/**
 * 用户ID加解密
 *
 * @author song.j
 * @create 2017-05-18 17:17:04
 **/
public class NumUtil {

    private static final Integer PRIVATE_KEY = 172131;


    private static final char[] LETTER = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };


    /**
     * 加密
     *
     * @param password
     * @return
     */
    public String ncrypt(Integer password) {

        //位数运算之后的密码
        Integer enpwd = password ^ PRIVATE_KEY;

        //位运算之后密文数据
        char[] enpwdChar = enpwd.toString().toCharArray();

        StringBuffer ncryptPwd = new StringBuffer();

        for (int i = 0; i < enpwdChar.length; i++) {
            char a = LETTER[Integer.valueOf(String.valueOf(enpwdChar[i]))];
            ncryptPwd.append(a);
        }

        return ncryptPwd.toString();
    }

    /**
     * 解密
     *
     * @param password
     * @return
     */
    public Integer deCrypt(String password) {

        char[] deChar = password.toCharArray();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < deChar.length; i++) {
            char indexChar = deChar[i];
            for (int j = 0; j < LETTER.length; j++) {
                if (LETTER[j] == indexChar) {
                    sb.append(j);
                    break;
                }
            }
        }
        return Integer.valueOf(sb.toString()) ^ PRIVATE_KEY;
    }

    public static void main(String[] args) {

        NumUtil numUtil = new NumUtil();
//
//        Integer password = 12345;
//
//        Integer enP = numUtil.ncrypt(password);
//
//        System.out.println(enP);
//
//        System.out.println("-------");
//
//        System.out.println(numUtil.deCrypt(enP));

        String en = numUtil.ncrypt(183253);

        System.out.println(en);
//
        System.out.println(numUtil.deCrypt(en));

    }
}

