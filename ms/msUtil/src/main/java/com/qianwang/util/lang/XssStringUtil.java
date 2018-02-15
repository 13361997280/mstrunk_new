package com.qianwang.util.lang;

/**
 * Created by songjie on 16/10/19.
 */
public class XssStringUtil {


    public String deXssFilter(String value) {


        value = value.replaceAll("&quot", "\"");
        value = value.replaceAll("& lt;", "<").replaceAll("& gt;", ">");
        value = value.replaceAll("& #40;", "\\(").replaceAll("& #41;", "\\)");
        value = value.replaceAll("& #39;", "'");
//        value = value.replaceAll("eval\\((.*)\\)", "");
//        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//        value = value.replaceAll("script", "");
        return value;
    }
}
