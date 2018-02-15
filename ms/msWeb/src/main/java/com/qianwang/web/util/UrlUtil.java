package com.qianwang.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author song.j
 * @create 2017-05-18 16:16:05
 **/
public class UrlUtil {


    public static List<String> getUrl(String content) {
        String reg = "(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(content);

        List<String> resultUrl = new ArrayList<String>();
        while (m.find()) {
            resultUrl.add(m.group());
        }
        return resultUrl;
    }


    public static String getUrlOne(String content) throws IllegalAccessException {
        List<String> urlList = getUrl(content);
        if (urlList.size() > 1) {
            throw new IllegalAccessException("content have " + urlList.size() + " url");
        }
        if (urlList.size() == 0)
            return null;

        return urlList.get(0);
    }

    public static void main(String[] args) {
        UrlUtil urlUtil = new UrlUtil();

        String body = "会做最好的农业网站，http://www.tngou.net/123/325/sc/121ds5.do们会做最好的农业网站 <br>请关注@天狗网 网站htp://www.tngou" +
                ".net/news/show/1?id=2&cd=chenle做做好的农业网站，加油。&nbsp;&nbsp;</p><p>坚信会";
//        String body = "会做最好的农业网站，ht会做最好的农业网站 <br>请关注@天狗网 网站http://www.tshow/1?id=2&cd=chenle做做好的农业网站，加油。&nbsp;&nbsp;</p><p>坚信会";

        try {
            System.out.println(urlUtil.getUrlOne(body));
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean invokeUrl(String url) {
        Pattern pattern = Pattern
                .compile("^((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\" +
                        ".[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-#()!]*)?");

        return pattern.matcher(url.trim()).matches();
    }

}
