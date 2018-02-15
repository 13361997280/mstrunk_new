package com.qbao.search.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LikeFilter {

	public boolean match(String[] kwds, String url) {
		for (String kwd : kwds) {
			if (url.contains(kwd) & !kwd.isEmpty())
				return true;
		}
		return false;
	}

	/** 邮件正则法则 **/
	public static String mailRegex = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+.[a-zA-Z0-9_-]+";
	/** website正则法则 **/
	public static String websiteRegex = "www.[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+";
	/** 国内电话号码正则法则 **/
	public static String telRegex = "[0-9]{3}-[0-9]{8}|[0-9]{4}-[0-9]{7}";
	/** 地址正则法则 [\u4e00-\u9fa5A-Za-z0-9]+[ ]{1} **/
	public static String addressRegex = "地址：[ ]*[\u4e00-\u9fa5A-Za-z0-9#——_-]+[ ]*";

	// 从一段文本中找出正则匹配的的字符串
	public static ArrayList<String> findFoRegex(String line, String regex) {
		ArrayList<String> finds = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);
		while (m.find()) {
			System.out.println(m.group());
			finds.add(m.group());
		}
		return finds;
	}

	public static void main(String[] args) {

		ArrayList<String> lst = new FileUtil().getText("d:\\51companyEbayDetail.txt");

		for (String line : lst) {
			LikeFilter.findFoRegex(line, mailRegex);
		}
		// LikeFilter.findFoRegex("公地址：福泉路asdf@sina.com 3838号10-1302  司021-55555555地0977-9898989址：浦东www.rp-china.cn张杨地址：大幅路3838号10-1302 路近afa@gmai.com源深 http://www.sina.com路 邮箱：shanghaiweitaih@sohu.com  ",
		// addressRegex);
	}

}
