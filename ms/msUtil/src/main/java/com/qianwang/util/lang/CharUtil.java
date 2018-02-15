package com.qianwang.util.lang;

import java.util.regex.Pattern;

/**
 * Created by songjie on 16/10/25.
 */
public class CharUtil {

    public static void main(String[] args) {
//        String[] strArr = new String[] { "www.micmiu.com", "!@#$%^&*()_+{}[]|\"'?/:;<>,.", "！￥……（）——：；“”‘’《》，。？、", "不要啊", "やめて", "韩佳人", "???" };
//        for (String str : strArr) {
//            System.out.println("===========> 测试字符串：" + str);
//            System.out.println("正则判断结果：" + isChineseByREG(str) + " -- " + isChineseByName(str));
//            System.out.println("Unicode判断结果 ：" + isChinese(str));
//            System.out.println("详细判断列表：");
//            char[] ch = str.toCharArray();
//            for (int i = 0; i < ch.length; i++) {
//                char c = ch[i];
//                System.out.println(c + " --> " + (isChinese(c) ? "是" : "否"));
//            }
//        }

        String str = ",";
//
        System.out.println(isEnglish(replaceChinese(str)));
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!isChinese(c)) {
                return false;
            }
        }
        return true;
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }

    /**
     * 判断字符串是否英文
     *
     *
     *
     * @param charaString
     * @return
     */
    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[0-9a-zA-Z*/ -()（）,.]*");
    }

    /**
     * 字符串去中文
     *
     * @param str 字符串
     * @return
     */
    public static String replaceChinese(String str) {

        StringBuffer sb = new StringBuffer();

        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!isChinese(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
