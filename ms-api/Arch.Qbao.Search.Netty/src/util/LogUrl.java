package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @author: hz
 * @date: url factory
 */

public class LogUrl {
	
	

	/*public String getUrl(String appId, String logLevel, String fromDate, String toDate) {
		return Clog_BASE_URL + appId + "?fromDate=" + fromDate + "&toDate=" + toDate + "&logLevel=" + logLevel;
	}*/

	public String getUrl(String Base_Url, String appId, String logLevel, String fromDate, String toDate) {
		String url = "";		
		try {
			url =  Base_Url + appId + "?fromDate=" + URLEncoder.encode(fromDate, "utf-8") + "&toDate=" + URLEncoder.encode(toDate, "utf-8") + "&logLevel=" + logLevel;
		} catch (UnsupportedEncodingException e) {
			//logger.error(e);
		}
		return url;
	}

	public String getNowUrl(String Base_Url , String appId, String logLevel, int intevelMillisecond) {
		String url = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateAtMin = DateUtil.DayFloorMin(new Date());
			String fromDate = sdf.format(dateAtMin.getTime() - intevelMillisecond);
			String toDate = sdf.format(dateAtMin.getTime());
			url = Base_Url + appId + "?fromDate=" + URLEncoder.encode(fromDate, "utf-8") + "&toDate="
					+ URLEncoder.encode(toDate, "utf-8") + "&logLevel=" + logLevel;

		} catch (Exception e) {
			//logger.error(e);
		}
		return url;
	}
	
	
	public String getUrl(String Base_Url  , String appId, String logLevel, Date toDate , int intevelMillisecond) {
		String url = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateAtMin = DateUtil.DayFloorMin(toDate);
			String fromDate = sdf.format(dateAtMin.getTime() - intevelMillisecond);
			String _toDate = sdf.format(dateAtMin.getTime());
			url = Base_Url + appId + "?fromDate=" + URLEncoder.encode(fromDate, "utf-8") + "&toDate="
					+ URLEncoder.encode(_toDate, "utf-8") + "&logLevel=" + logLevel;

		} catch (Exception e) {
			//logger.error(e);
		}
		return url;
	}

	public static void main(String[] args) {
		
//		 String Clog_BASE_URL = Config.get().get("clog.base.url",
//				"http://rest.logging.sh.ctriptravel.com/data/logs/");
//		LogUrl lu = new LogUrl();
//		String url = lu.getUrl(Clog_BASE_URL,"920601", "1", "2014-02-08%2010:02:22", "2014-02-08%2010:02:33");
//		System.out.println(url);
//		url = lu.getUrl(Clog_BASE_URL,"920601", "3", "2014-02-08%2010:02:01", "2014-02-08%2010:02:01");
//		System.out.println(url);
//		System.out.println("==decode: "+URLDecoder.decode(url));
//		
//		url = lu.getNowUrl(Clog_BASE_URL,"920666", "1", 60000);
//		System.out.println(url);
//		System.out.println("==decode: "+ URLDecoder.decode(url));
//		
//		url = lu.getUrl(Clog_BASE_URL,"920666", "1", new Date(), 60000);
//		System.out.println(url);
//		System.out.println("==decode: "+ URLDecoder.decode(url));
		
	}

}
