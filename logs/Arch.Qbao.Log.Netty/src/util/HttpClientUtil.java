package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/************
 * Http请求调用通用方法
 * @author guofei date:2013-08-15
 */

public class HttpClientUtil {

	/******
	 * 获取新的client信息
	 * @return HttpClient
	 */
	public static HttpClient getNewClient() {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		return client;
	}
	/********
	 * 通用关闭client方法,一般使用结束需手动调用
	 * @param client
	 */
	public static void close(HttpClient client) {
		try {
			if (client != null)
				client.getConnectionManager().shutdown();
		} catch (Exception e) {
		}
	}
	/*********
	 * 获取cookie
	 * @param client 请求client
	 * @return cookie的List列表
	 */
	public static List<Cookie> getCookie(HttpClient client){
		return ((AbstractHttpClient)client).getCookieStore().getCookies();
	}
	/*********
	 * 添加cookie信息
	 * @param client 请求client
	 * @param cookie 需要添加的cookie信息
	 * @return 成功/失败
	 */
	public static boolean addCookie(HttpClient client,Cookie cookie){
		((AbstractHttpClient)client).getCookieStore().addCookie(cookie);
		return true;
	}

	
	/**********************我是分割线********************get请求********************************************/
	/**********
	 * get请求 无参数
	 * @param url 调用url
	 * @return 页面
	 */
	public static String doGet(String url) {
		HttpClient client = getNewClient();
		String res = null;
		try {
			res = doGet(client, url, null);
		} finally {
			close(client);
		}
		return res;
	}
	/**********
	 * get请求 无参数
	 * @param client 客户端
	 * @param url 调用url
	 * @return 页面
	 */
	public static String doGet(HttpClient client, String url) {
		return doGet(client, url, null);
	}
	/**********
	 * get请求
	 * @param client 客户端
	 * @param url 调用url
	 * @param params 请求参数值key1=value1&key2=values....
	 * @return 页面
	 */
	public static String doGet(HttpClient client, String url, String params) {
		return doGet(client, url, params, null);
	}

	/**********
	 * get请求
	 * @param client 客户端
	 * @param url 调用url
	 * @param params 请求参数值key1=value1&key2=values....
	 * @param respcharSet 指定返回结果的编码类型
	 * @return 页面
	 */
	public static String doGet(HttpClient client, String url, String params,String respcharSet) {
		try {
			if (params != null && params.isEmpty() == false) {
				url = url + "?" + params;
			}
			HttpGet httpGet = new HttpGet(url);
			String res;
			synchronized (client) {
				HttpResponse response = client.execute(httpGet);
				HttpEntity httpEntity = response.getEntity();
				res = getResult(httpEntity, respcharSet);
			}
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**********
	 * 调用地址并保存进文件
	 * @param url 调用url
	 * @param filePath 指定返回结果的编码类型
	 * @return 成功/失败
	 */
	public static boolean doGetToFile(String url,String filePath) {
		HttpClient client = getNewClient();
		try {
			return doGetToFile(client, url, filePath);
		} finally {
			close(client);
		}
	}
	/**********
	 * 调用地址并保存进文件
	 * @param client client
	 * @param url 调用url
	 * @param filePath 指定返回结果的编码类型
	 * @return 成功/失败
	 */
	public static boolean doGetToFile(HttpClient client, String url,String filePath) {
		try {
			HttpGet httpGet = new HttpGet(url);
			synchronized (client) {
				HttpResponse response = client.execute(httpGet);
				HttpEntity httpEntity = response.getEntity();
				return saveToFile(httpEntity, filePath);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**********************我是分割线********************Post请求********************************************/
	
	/*******
	 * 不需要client的post请求
	 * @param url 请求地址
	 * @param params 请求参数 key1=value1&key2=value2.....
	 * @return 页面信息
	 */
	public static String doPost(String url, String params) {
		return doPost(url, params, null);
	}
	
	/**********
	 * 不需要client的post请求
	 * @param url 请求地址
	 * @param params 请求参数 key1=value1&key2=value2.....
	 * @param charset 请求参数编码格式
	 * @return 页面
	 */
	public static String doPost(String url, String params, String charset) {
		Map<String, String> para = null;
		if (params != null && params.isEmpty() == false) {
			para = new HashMap<String, String>();
			String[] pps = params.split("&");
			for (String p : pps) {
				String[] kv = p.split("=");
				if(kv.length<2){
					para.put(kv[0],"");
				}else{
					para.put(kv[0],kv[1]);
				}
			}
		}
		return doPost(url, para, charset);
	}
	/*********
	 * 不需要client的post请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 页面信息
	 */
	public static String doPost(String url, Map<String, String> params) {
		return doPost(url, params,null);
	}
	/*********
	 * 不需要client的post请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 请求编码格式
	 * @return 页面信息
	 */
	public static String doPost(String url, Map<String, String> params,String charset) {
		HttpClient client = getNewClient();
		String res = null;
		try {
			res = doPost(client, url, params, charset,null,null);
		} finally {
			close(client);
		}
		return res;
	}
	/*********
	 * 需要client的post请求
	 * @param client 指定客户端
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,String params) {
		Map<String, String> para = null;
		if (params != null && params.isEmpty() == false) {
			para = new HashMap<String, String>();
			String[] pps = params.split("&");
			for (String p : pps) {
				String[] kv = p.split("=");
				if(kv.length<2){
					para.put(kv[0],"");
				}else{
					para.put(kv[0],kv[1]);
				}
			}
		}
		return doPost(client,url, para);
	}
	/*********
	 * 需要client的post请求
	 * @param client 指定客户端
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,Map<String, String> params) {
		return doPost(client, url, params, null);
	}
	/*********
	 * 需要client的post请求
	 * @param client 指定客户端
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 请求编码格式
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,Map<String, String> params,String charset) {
		return doPost(client, url, params, charset,"");
	}
	/*********
	 * 需要client的post请求
	 * @param client 指定客户端
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 请求编码格式
	 * @param respcharSet 指定返回结果的编码格式 为空则读取response中的charset,当为空标示用utf-8
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,Map<String, String> params, String charset,String respcharSet) {
		return doPost(client, url, params, charset, null, respcharSet);
	}
	/*********
	 * 需要client的post请求
	 * @param client 指定客户端
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 请求编码格式
	 * @param headers 请求header信息
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,Map<String, String> params, String charset,Map<String, String> headers) {
		return doPost(client, url, params, charset, headers, null);
	}

	/********
	 * 发起Post请求
	 * @param client 客户端信息
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 编码格式 为空表是utf-8
	 * @param headers 需要添加的head信息,如特殊cookie等
	 * @param respcharSet 指定返回结果的编码格式 为空则读取response中的charset,当为空标示用utf-8
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,Map<String, String> params,
			String charset,Map<String, String> headers, String respcharSet) {
		try {
			List<NameValuePair> nvs = null;
			if (params != null && params.isEmpty() == false) {
				nvs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> pair : params.entrySet()) {
					nvs.add(new BasicNameValuePair(pair.getKey(), pair.getValue()));
				}
			}
			synchronized (client) {
				HttpEntity httpEntity = excuteAndGetEntity(client, url, nvs, charset, headers);
				return getResult(httpEntity, respcharSet);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/********
	 * 发起Post请求
	 * @param client 客户端信息
	 * @param url 请求地址
	 * @param nvs NameValuePair型的参数列表
	 * @param charset 编码格式 为空表是utf-8
	 * @param headers 需要添加的head信息,如特殊cookie等
	 * @param respcharSet 指定返回结果的编码格式 为空则读取response中的charset,当为空标示用utf-8
	 * @return 页面信息
	 */
	public static String doPost(HttpClient client, String url,List<NameValuePair> nvs,
			String charset,Map<String, String> headers, String respcharSet) {
		try {
			synchronized (client) {
				HttpEntity httpEntity = excuteAndGetEntity(client, url, nvs, charset, headers);
				return getResult(httpEntity, respcharSet);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**********
	 * 调用地址并保存进文件
	 * @param url 调用url
	 * @param params 请求参数
	 * @param filePath 保存文件地址
	 * @return true/false
	 */
	public static boolean doPostToFile(String url,Map<String, String> params,String filePath){
		return doPostToFile(url, params, filePath, null, null);
	}
	/**********
	 * 调用地址并保存进文件
	 * @param url 调用url
	 * @param params 请求参数
	 * @param filePath 保存文件地址
	 * @param charset 请求参数的编码格式
	 * @param headers headers
	 * @return true/false
	 */
	public static boolean doPostToFile(String url,Map<String, String> params,String filePath
			,String charset,Map<String, String> headers) {
		HttpClient client = getNewClient();
		try {
			return doPostToFile(client, url,params,filePath,charset,headers);
		} finally {
			close(client);
		}
	}
	/**********
	 * 调用地址并保存进文件
	 * @param client 客户端
	 * @param url 调用url
	 * @param params 请求参数
	 * @param filePath 保存文件地址
	 * @return true/false
	 */
	public static boolean doPostToFile(HttpClient client,String url,Map<String, String> params,String filePath){
		return doPostToFile(client,url, params, filePath, null, null);
	}
	/**********
	 * 调用地址并保存进文件
	 * @param client 客户端
	 * @param url 调用url
	 * @param params 请求参数
	 * @param filePath 保存文件地址
	 * @param charset 请求参数的编码格式
	 * @param headers headers
	 * @return true/false
	 */
	public static boolean doPostToFile(HttpClient client, String url,Map<String, String> params,String filePath
			,String charset,Map<String, String> headers) {
		try {
			List<NameValuePair> nvs = null;
			if (params != null && params.isEmpty() == false) {
				nvs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> pair : params.entrySet()) {
					nvs.add(new BasicNameValuePair(pair.getKey(), pair.getValue()));
				}
			}
			synchronized (client) {
				HttpEntity httpEntity = excuteAndGetEntity(client, url, nvs, charset, headers);
				return saveToFile(httpEntity, filePath);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**********
	 * 调用地址并保存进文件
	 * @param client 客户端
	 * @param url 调用url
	 * @param nvs NameValuePair类型的请求参数
	 * @param filePath 保存文件地址
	 * @param charset 请求参数的编码格式
	 * @param headers headers
	 * @return true/false
	 */
	public static boolean doPostToFile(HttpClient client, String url,List<NameValuePair> nvs,String filePath
			,String charset,Map<String, String> headers) {
		try {
			synchronized (client) {
				HttpEntity httpEntity = excuteAndGetEntity(client, url, nvs, charset, headers);
				return saveToFile(httpEntity, filePath);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/********************** 我是分割线********************文件上传请求 ********************************************/
	/********
	 * 上传文件
	 * @param client 客户端
	 * @param url 上传地址
	 * @param paramName 上传文件参数名
	 * @param file 上传文件
	 * @return 返回结果
	 */
	public static String upload(HttpClient client, String url,String paramName, File file) {
		Map<String, File> files = new HashMap<String, File>();
		files.put(paramName, file);
		return upload(client, url, files, null);
	}
	/********
	 * 上传文件
	 * @param client 客户端
	 * @param url 上传地址
	 * @param files 上传文件列表
	 * @param params 上传请求附加参数 可为空
	 * @return 返回结果
	 */
	public static String upload(HttpClient client, String url,Map<String, File> files, Map<String, String> params) {
		try {
			HttpPost httppost = new HttpPost(url);
//			httppost.setHeader("Content-Type","audio/L16; rate=16000");//测试上传
			MultipartEntity reqEntity = new MultipartEntity();
			for (Map.Entry<String, File> file : files.entrySet()) {
				reqEntity.addPart(file.getKey(), new FileBody(file.getValue()));
			}
			if(params != null){
				for (Map.Entry<String, String> param : params.entrySet()) {
					reqEntity.addPart(param.getKey(),new StringBody(param.getValue()));
				}
			}
			httppost.setEntity(reqEntity);
			synchronized (client) {
				HttpResponse response = client.execute(httppost);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity resEntity = response.getEntity();
					String result = getResult(resEntity, null);
					//关闭数据流
					EntityUtils.consume(resEntity);
					return result;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	/*********************我是分割线*****************返回结果处理***************************************************/
	
	/********
	 * 从 HttpEntity中获取String类型的结果 1.解压 2.字符类型判断 3.大小判断
	 * @param entity返回对象
	 * @param respCharSet指定返回结果的编码类型
	 * @return 处理后的结果字符串
	 * @throws Exception
	 */
	private static String getResult(HttpEntity entity, String respCharSet)throws Exception {
		String html = "";
		Header header = entity.getContentEncoding();
		InputStream in = entity.getContent();
		if (respCharSet == null|| respCharSet.isEmpty()) {
			Charset charset = ContentType.getOrDefault(entity).getCharset();
			if (charset != null) {
				respCharSet = charset.toString();
			} else {
				respCharSet = "utf-8";
			}
		}
		if (header != null && "gzip".equals(header.getValue())) {
			html = unZip(in, respCharSet);
		} else {
			html = readInStreamToString(in, respCharSet);
		}
		if (in != null) {
			in.close();
		}
		return html;
	}

	/**
	 * 解压服务器返回的gzip流
	 * @param in 抓取返回的InputStream流
	 * @param charSet 页面内容编码
	 * @return 页面内容的String格式
	 * @throws IOException
	 */
	private static String unZip(InputStream in, String charSet)throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPInputStream gis = null;
		try {
			gis = new GZIPInputStream(in);
			byte[] _byte = new byte[1024];
			int len = 0;
			while ((len = gis.read(_byte)) != -1) {
				baos.write(_byte, 0, len);
			}
			String unzipString = new String(baos.toByteArray(), charSet);
			return unzipString;
		} finally {
			if (gis != null) {
				gis.close();
			}
			if (baos != null) {
				baos.close();
			}
		}
	}

	/**
	 * 读取InputStream流
	 * @param in InputStream流
	 * @return 从流中读取的String
	 * @throws IOException
	 */
	private static String readInStreamToString(InputStream in, String charSet)throws IOException {
		StringBuilder str = new StringBuilder();
		String line;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charSet));
		while ((line = bufferedReader.readLine()) != null) {
			str.append("\n");
			str.append(line);
		}
		if (bufferedReader != null) {
			bufferedReader.close();
		}
		if(str.length()>0){
			return str.substring(1);
		}else
			return str.toString();
	}
	
	/*********
	 * 根据参数执行Post请求并返回HttpEntity
	 * @param client 客户端
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param charset 请求参数编码格式
	 * @param headers headers
	 * @return 返回对象
	 */
	private static HttpEntity excuteAndGetEntity(HttpClient client,String url,List<NameValuePair> nvs,String charset,Map<String, String> headers)
			throws ClientProtocolException, IOException{
		if (charset == null || charset.isEmpty())
			charset = "utf-8";
		UrlEncodedFormEntity entity = null;
		if (nvs != null && nvs.isEmpty() == false) {
			entity = new UrlEncodedFormEntity(nvs, charset);
		}
		HttpPost httppost = new HttpPost(url);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httppost.addHeader(header.getKey(), header.getValue());
			}
		}
		if (entity != null) {
			httppost.setEntity(entity);
		}
		HttpResponse response = client.execute(httppost);
		return response.getEntity();
	}
	
	/************
	 * 将返回对象保存进文件
	 * @param httpEntity 返回对象
	 * @param filePath 保存文件地址
	 * @return 成功or失败
	 */
	private static boolean saveToFile(HttpEntity httpEntity,String filePath) throws IllegalStateException, IOException{
		if(httpEntity==null) return false;
		InputStream in = httpEntity.getContent();
		File f = new File(filePath);
		FileOutputStream out = new FileOutputStream(f);
		int len = 1024;
		byte[] bArray = new byte[len];
		int retVal = 0;
		while ((retVal = in.read(bArray, 0, len)) != -1) {
			out.write(bArray, 0, retVal);
		}
		out.flush();
		out.close();
		//关闭数据流
		EntityUtils.consume(httpEntity);
		return true;
	}
	
	public static void main(String[] args) {
		String url,data;
		HttpClient client = null;
		url="http://www.baidu.com";
		
		//直接调用
		data = HttpClientUtil.doGet(url);
		System.out.println(data);
		
		//通过client调用(便于用同一个client做多次请求,如模拟登陆)
		client = HttpClientUtil.getNewClient();
		data = HttpClientUtil.doGet(client,url);
		System.out.println(data);
		
	}
}
