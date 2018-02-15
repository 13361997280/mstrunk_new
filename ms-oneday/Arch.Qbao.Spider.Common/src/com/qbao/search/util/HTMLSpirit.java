package com.qbao.search.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern; 

public class HTMLSpirit{ 
    public static String delHTMLTag(String htmlStr){ 
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
         
        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script=p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); //过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); //过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    }
    public static void main(String[] args){
        String str = "&lt;img src=&quot;http://p3.pstatp.com/large/310b00030e663a967827&quot; img_width=&quot;1012&quot; img_height=&quot;380&quot; alt=&quot;继续发声！驻印使馆向印媒介绍中方立场：勿低估中国决心！&quot; inline=&quot;0&quot;&gt;&lt;p&gt;【环球网综合报道】据中国驻印度大使馆官网8月4日消息,8月3日,中国驻印度使馆刘劲松公使在使馆向《印度教徒报》、《新印度时报》、《亚洲世纪报》、《德干先驱报》和“火线”网站记者吹风并答问,全面介绍8月2日中国外交部发表的《印度边防部队在中印边界锡金段越界进入中国领土的事实和中国的立场》,并回答记者相关提问。中方必须再次敦促印方立即无条件撤出越界边防部队,敦促印方不要低估中国政府和人民捍卫领土主权的决心。&lt;/p&gt;&lt;img src=&quot;http://p3.pstatp.com/large/310b00023856d5d426ed&quot; img_width=&quot;850&quot; img_height=&quot;566&quot; alt=&quot;继续发声！驻印使馆向印媒介绍中方立场：勿低估中国决心！&quot; inline=&quot;0&quot;&gt;&lt;p&gt;使馆新闻参赞谢立艳、政务参赞李亚在座。&lt;/p&gt;&lt;p&gt;刘指出,立场文件从历史、法理、国际关系等各个角度,权威、全面、严肃地阐述了中方对印军越界事件的立场。&lt;/p&gt;&lt;p&gt;之所以要发表这一文件,一是因为国际社会包括印各界人士希望了解事件的来龙去脉、是非曲直和现实情况,希望知晓中国政府系统完整的立场及其缘由,希望在此基础上思考解决此次事件的正确路径。&lt;/p&gt;&lt;p&gt;二是一段时间以来,印方一些人士炮制了各种站不住的理由,混淆视听,为印军非法越界编造借口,印外交部还于6月30日发表了一个声明,中方必须以正视听。&lt;/p&gt;&lt;p&gt;三是印军侵入中国领土已逾一个半月,迄今仍有48名印方军人和1台推土机非法滞留。鉴于印方没有采取任何实际行动纠正错误,中方必须再次敦促印方立即无条件撤出越界边防部队,敦促印方不要低估中国政府和人民捍卫领土主权的决心。&lt;/p&gt;&lt;p&gt;中方这么做,也为维护国际法秩序和国际关系准则,维护国际正义。立场文件用严谨、负责的语言及事实,回应了国际社会的期待,回答了一些印度朋友的提问,希望印各界朋友重视和研究。&lt;/p&gt;";
        str = str.replace("&lt;","<");
        str = str.replace("&gt;",">");
        str = str.replace("&quot;","\"");
        System.out.println(str);
        System.out.println(delHTMLTag(str));
    }
} 