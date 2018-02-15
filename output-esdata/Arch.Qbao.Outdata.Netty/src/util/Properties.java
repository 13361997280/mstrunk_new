package util;

import java.io.File;

import com.qbao.search.conf.Config;
import com.qbao.search.util.NetworkUtils;

public class Properties {

	// port
	public static final int port = Integer.parseInt(Config.get().get("engine.port"));

	// web url root ${xxx}

	public static final String rootUrl = "http://" + NetworkUtils.getLocalIP() + ":" + port + "/";
	public static final String cssUrl = rootUrl + "css?path=";
	public static final String jsUrl = rootUrl + "js?path=";
	public static final String picUrl = rootUrl + "pic?path=";

	// Root.path source file path : css, js ,pic, template
	public static final String rootPath = new File(System.getProperty("user.dir")).getParentFile().getPath();
	public static final String cssPath = rootPath + "/web/dooioo/css/";
	public static final String jsPath = rootPath + "/web/dooioo/js/";
	public static final String picPath = rootPath + "/web/dooioo/pic/";
	public static final String templatePath = rootPath + "/web/dooioo/template/";

	public static String HtmlParse(String htmlText) {
		htmlText = htmlText.replace("${rootUrl}", rootUrl);
		htmlText = htmlText.replace("${cssUrl}", cssUrl);
		htmlText = htmlText.replace("${jsUrl}", jsUrl);
		htmlText = htmlText.replace("${picUrl}", picUrl);
		return htmlText;

	}

	public static void main(String[] args) {

	}
}
