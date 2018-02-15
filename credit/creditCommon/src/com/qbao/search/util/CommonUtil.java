package com.qbao.search.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2011-8-5 10:02:24
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-8-5 10:02:24  	li_yao		1.0			Newly created
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonUtil {
	
	public static int SYSNO = 0;
	
	public static void setSysNo(int sysno){
		SYSNO=sysno;
		//update CentralLogging ConfigAppID
		//LogConfig.setAppID(String.valueOf(SysNoMapping.getNewSysNo(SYSNO)));
	}
	public static String SEREVER_NAME = null;

	final private static ESLogger logger = Loggers.getLogger(CommonUtil.class);
	

	public static final DateFormat DATE_FORMAT = 
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	final public static String DIR_FS = "FS";
	
	final public static String DIR_RAM = "RAM";
	
	final public static String DIR_MMAP = "MMAP";

	final public static String PARAM_HEADER = "param2header_";

	
	public static String formatTime(long timeStamp) {
		return DATE_FORMAT.format(new Date(timeStamp));
	}
	
	public static long getTime(String dateStr) throws ParseException {
		return DATE_FORMAT.parse(normalizeDate(dateStr)).getTime();
	}
	
	public static String normalizeDate(String dateStr) {
		int num = 3 - (dateStr.length() -1- dateStr.indexOf('.'));
		for(int i = 0; i < num; i++) {
			dateStr += '0';
		}
		return dateStr;
	}

	
	public static final String toString(Throwable t) {
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PrintWriter p = new PrintWriter(ba);
		t.printStackTrace(p);
		p.flush();
		return ba.toString();
	}
	
	public static String reverseString(String str){
		StringBuilder sb = new StringBuilder(str.length());
		for(int i = str.length() - 1; i >= 0; i--){
			sb.append(str.charAt(i));
		}
		return sb.toString();
	}

	public static boolean booleanValue(String val){
		if(val == null){
			return false;
		}
		if(val.equalsIgnoreCase("true")){
			return true;
		}
		if(val.equalsIgnoreCase("false")){
			return false;
		}
		if(val.equalsIgnoreCase("yes")){
			return true;
		}
		if(val.equalsIgnoreCase("no")){
			return false;
		}
		if(val.equalsIgnoreCase("y")){
			return true;
		}
		if(val.equalsIgnoreCase("n")){
			return false;
		}
		if(val.equals("1")){
			return true;
		}
		if(val.equals("0")){
			return false;
		}
		return false;
	}
	
	public static int[] genFromTo(String val){
		int from = 1;
		int to = Integer.MAX_VALUE;
		String regex = "^(\\d*)([^0-9]+)(\\d*)$";
		Pattern p = Pattern.compile(regex);
		if(val != null) {
			Matcher m = p.matcher(val);
			if(m.find()) {
				if(m.group(1) != null && !m.group(1).isEmpty()) {
					from = Integer.parseInt(m.group(1));
				}
				if(m.group(3) != null && !m.group(3).isEmpty()) {
					to = Integer.parseInt(m.group(3));
				}
			} else if(!val.isEmpty()){
				to = Integer.parseInt(val);
			}
		}
		return new int[]{from, to};
	}
	
	
	public static String getParam(HttpRequest httpRequest, String param) 
			throws UnsupportedEncodingException{
		 return getParam(httpRequest,param,"UTF-8");
	}
	
	public static boolean getBooleanParam(HttpRequest httpRequest, String param)
			throws UnsupportedEncodingException{
		String str = getParam(httpRequest, param, "UTF-8");
		return str != null && str.equalsIgnoreCase("true");
	}
	public static String getByParam(String uri, String param,
			String encoding) throws UnsupportedEncodingException {
		
		String url = null;
		int pos = uri.indexOf("?");
		if( pos == -1 ) {
			url = uri;
		} else {
			url = uri.substring(pos+1);
		}
		
		String[] str = url.split("&");
		for( String s : str ) {
			String[] cips = s.split("=");
			if( cips[0].equals(param) ) {
				return URLDecoder.decode(cips[1],encoding);
			}
		}
		
		return null;
	}
	
	public static String getParam(HttpRequest httpRequest, String param,
			String encodeing) throws UnsupportedEncodingException {

		if(param == null) {
			return null;
		}
		param = param.toLowerCase();
		if(httpRequest.getHeader(PARAM_HEADER) != null) {
			return httpRequest.getHeader(PARAM_HEADER + param);
		}
		Map<String, List<String>> paramMap = new QueryStringDecoder(
				URLDecoder.decode(httpRequest.getUri(), encodeing), 
				Charset.forName(encodeing)).getParameters();

		for(Entry<String, List<String>> entry:paramMap.entrySet()) {
			if(entry.getKey().toLowerCase().equals(param)) {
				if(entry.getValue() != null && entry.getValue().size() > 0) {
					return entry.getValue().get(0);
				}
				return null;
			}
		}
		return null;
	}
	public static Map getParamMap(HttpRequest httpRequest) {
		Map requestParams = new LinkedHashMap();
		// 处理get请求
		try {
			Map<String, List<String>> parame = new QueryStringDecoder(
					URLDecoder.decode(httpRequest.getUri(), "UTF-8"),
					Charset.forName("UTF-8")).getParameters();
			Iterator<Entry<String, List<String>>> iterator = parame.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<String>> next = iterator.next();
				String value = next.getValue().get(0);
				if("".equals(value)||value==null){
					continue;
				}
				value = value.trim();
				if(value!=null&&!"".equals(value.trim())) {
					requestParams.put(next.getKey(), value.trim());
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return requestParams;
	}
	public static Map<String,String> deepCloneMap(Map<String,String> src){
		Map<String,String> des=new HashMap();
		Iterator<Entry<String, String>> iterator = src.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String> entity = iterator.next();
			String key=entity.getKey();
			String value=entity.getValue();
			des.put(key,value);
		}
		return des;
	}
	public static Map<String,String> convertMap(String src){
		Map<String,String> des = new HashMap();
		if(null!=src&&!"".equals(src)){
			String[] srcA = src.split("&");
			if(srcA.length>1){
				for(int i = 0;i<srcA.length;i++){
					String[] el = srcA[i].split("=");
					if(el.length>1) {
						if(null!=el[1]&&!"".equals(el[1])) {
							des.put(el[0], el[1]);
						}
					}
				}
			}else {
				String[] el = src.split("=");
				if(el.length>1) {
					if(null!=el[1]&&!"".equals(el[1])) {
						des.put(el[0], el[1]);
					}
				}
			}
		}
		return des;
	}
	public static String getParamStr(Map<String,String> parame) {
		String requestParams = "";
		// 处理get请求
		try {
			Iterator<Entry<String, String>> iterator = parame.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> next = iterator.next();
				String value = next.getValue();
				if("".equals(value)||value==null){
					continue;
				}
				requestParams=requestParams+next.getKey()+"="+value+"&";
			}
			if(!"".equals(requestParams)&&requestParams.contains("&")) {
				requestParams = requestParams.substring(0, requestParams.length() - 1);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return requestParams;

	}

	
	public static String getRemoteIP(HttpRequest httpRequest) {
		String ip = null;
		String[] headers = new String[]{"X-Forwarded-For","REMOTE_ADDR",
			"HTTP_CLIENT_IP", "HTTP_VIA"};
		for(String header:headers) {
			ip = httpRequest.getHeader(header);
			if(ip != null && !ip.isEmpty()) {
				return ip;
			}
		}
		return ip;
	}
	
	public static String getRemoteIP(Channel channel) {
		String ip = channel.getRemoteAddress().toString();
		return ip.substring(1, ip.indexOf(':'));
	}
	
	public static boolean addressEqual(String addr1, String addr2){
		int pos = addr1.indexOf(':');
		if(pos < 0){
			addr1 += ":80";
		}
		
		pos = addr2.indexOf(':');
		if(pos < 0){
			addr2 += ":80";
		}

		return addr1.equalsIgnoreCase(addr2);
	}
	
	
	
	
	public static <T> Class<? extends T> getClass(Class<? extends T> cls,
			String clsName) throws ClassNotFoundException{
		cls =  (Class<? extends T>) Class.forName(clsName);
		logger.info("use:{}", cls);
		return cls;
	}
	
	public static <T> Class<? extends T> getClass(Class<? extends T> cls,
			String clsName, Class<? extends T> defaultCls) {
		try {
			return getClass(cls, clsName);
		} catch (ClassNotFoundException e) {
			//logger.info("use default:{}", defaultCls);
			return defaultCls;
		}		
	}
	
	public static <T> T getObject(String clsName, T defaultObj) {
		try {
			return (T) Class.forName(clsName).newInstance();
		}  catch (Exception e) {
			return defaultObj;
		}
	}
	
	public static <T> Class<? extends T> getClass(Class<? extends T> cls,
			String indexName, String subPackage) throws ClassNotFoundException{
		String prefix = indexName.substring(0, 1).toUpperCase()
			+ indexName.substring(1).toLowerCase();
		return getClass(cls, "com.qbao.search." +
			indexName + "." + subPackage + "." + prefix + cls.getSimpleName());
	}
	
	public static <T> Class<? extends T> getClass(Class<? extends T> cls,
			String indexName, String subPackage, Class<? extends T> defaultCls){
		try {
			return getClass(cls, indexName, subPackage);
		} catch (ClassNotFoundException e) {
			logger.info("use default:{}", defaultCls);
			return defaultCls;
		}
	}
	
	public static int[] sequence(int arrLen){
		int[] arr = new int[arrLen];
		for(int i = 0; i < arrLen; i++){
			arr[i] = i;
		}
		Random rand = new Random();
		for(int i = 0; i < arrLen; i++){
			int r = rand.nextInt(arrLen);
			int tmp = arr[i];
			arr[i] = arr[r];
			arr[r] = tmp;
		}
		return arr;
	}
	
	public static Map<String, String> wrapMap(String prefix, 
			Map<String, String> subMap) {
		return wrapMap(prefix, null, subMap);
	}
	
	public static Map<String, String> wrapMap(String prefix,
			Map<String, String> map, Map<String, String> subMap) {
		if(map == null) {
			map = new HashMap<String, String>();
		}
		if(subMap != null){
			for(Entry<String, String> entry:subMap.entrySet()){
				map.put(prefix + entry.getKey(), entry.getValue());
			}
		}
		return map;
	}


	/**
	 * @param seperator
	 * @return
	 */
	public static String[] split(String str, char seperator) {
		if(str == null) {
			return null;
		}
		List<String> strs = new ArrayList<String>();
		int start = -1;
		for(int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if(ch == seperator) {
				if(start != -1) {
					strs.add(str.substring(start, i));
					start = -1;
				}
			}
			else {
				if(start == -1) {
					start = i;
				}
			}
		}
		if(start != -1) {
			strs.add(str.substring(start, str.length()));
		}
		return strs.toArray(new String[strs.size()]);
	}
	
    public static void sendMail(String subject, String body) 
    		throws MessagingException, UnsupportedEncodingException{
		String tos = Config.get().get("monitor.mail.tos", 
		"li_yao@ctrip.com,zzfu@ctrip.com,huangz@ctrip.com");
		String ccs = Config.get().get("monitor.mail.ccs", "");
    	//sendMail(subject, body, tos, ccs);
    }
	
    public static void sendMail(String subject, String body, String tos,
    		String ccs) throws MessagingException, UnsupportedEncodingException{
    	/*sendMail(Config.get().get("mail.smtp.host",
    		"appmail.sh.ctriptravel.com"), subject, body,
    		"search@ctrip.com", null, tos, ccs, null);*/
    }
	
    public static void sendMail(String smtpHost, String subject, String body,
    		String from, String personal, String tos, String ccs, String bccs)
    		throws MessagingException, UnsupportedEncodingException{
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost); 
        Session session = Session.getInstance(props); 
        MimeMessage message = new MimeMessage(session); 
        message.setSubject(subject);
        message.setText(body);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(from, personal));
        for(String to:tos.split(",")) {
        	message.addRecipients(Message.RecipientType.TO, to); 
        }
        if(ccs != null) {
	        for(String cc:ccs.split(",")) {
	        	message.addRecipients(Message.RecipientType.CC, cc); 
	        }
        }
        if(bccs != null) {
	        for(String bcc:bccs.split(",")) {
	        	message.addRecipients(Message.RecipientType.BCC, bcc); 
	        }
        }
        Transport.send(message);
    }
    
    //send html 
    public static void sendHtmlMail(String subject, String html, String tos,
    		String ccs) throws MessagingException, UnsupportedEncodingException{
    	/*sendHtmlMail(Config.get().get("mail.smtp.host",
    		"appmail.sh.ctriptravel.com"), subject, html,
    		"search@ctrip.com", null, tos, ccs, null);*/
    }
    
    //send HTML mail 
    public static void sendHtmlMail(String smtpHost, String subject, String html,
    		String from, String personal, String tos, String ccs, String bccs)
    		throws MessagingException, UnsupportedEncodingException{
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost); 
        Session session = Session.getInstance(props); 
        MimeMessage message = new MimeMessage(session); 
        message.setContent(html,"text/html;charset=gb2312");    
        message.setSubject(subject); 
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(from, personal));
        for(String to:tos.split(",")) {
        	message.addRecipients(Message.RecipientType.TO, to); 
        }
        if(ccs != null) {
	        for(String cc:ccs.split(",")) {
	        	message.addRecipients(Message.RecipientType.CC, cc); 
	        }
        }
        if(bccs != null) {
	        for(String bcc:bccs.split(",")) {
	        	message.addRecipients(Message.RecipientType.BCC, bcc); 
	        }
        }
        Transport.send(message);
    }
    
    
    public static String normalizeNumber(int len, char digit) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < len; i++) {
			sb.append(digit);
		}
		return sb.toString();
    }
    
	public static String normalizeNumber(int len, String numStr) {
		if(numStr == null) {
			numStr = "";
		}
		if(len < numStr.length()) {
			throw new IllegalArgumentException("length of numStr:" +numStr 
				+ " > subLen:" + len);
			//numStr = numStr.substring(numStr.length() - len);
		}
		else if(len > numStr.length()) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < len - numStr.length(); i++) {
				sb.append('0');
			}
			sb.append(numStr);
			numStr = sb.toString();
		}
		return numStr;
	}
	
	public static int getDaysDiff(String format, String first, String second)
			throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long from = df.parse(first).getTime();
		long to = df.parse(second).getTime();
		return (int) ((to - from) / (1000 * 60 * 60 * 24));
	}

	
	public static int getDaysDiff(String first, String second)
			throws ParseException {
		return getDaysDiff("yyyy-MM-dd", first, second);
	}	
	
	public static String getThreadPoolInfo(ThreadPoolExecutor e) {
		return "ActiveCount:" + e.getActiveCount()
		+ ",CompletedTaskCount:" + e.getCompletedTaskCount()
		+ ",TaskCount:" + e.getTaskCount()
		+ ",QueueSize:" + e.getQueue().size();
	}

	public static String inputStream2String(InputStream is) throws IOException { 
		OutputStream baos = new ByteArrayOutputStream(); 
		int i = -1; 
		while((i = is.read()) != -1){ 
			baos.write(i); 
		} 
		return baos.toString(); 
	}

}
