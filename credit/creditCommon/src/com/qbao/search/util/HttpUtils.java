package com.qbao.search.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.ESLoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author by zhangchanghong on 15/12/6.
 */
public class HttpUtils {

	public static <T> List<T> get(String url, Class<T> clazz) throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		try {

			httpGet.setURI(new URI(httpGet.getURI().toString()));
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
			return JSON.parseArray(body, clazz);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpGet.abort();
		}
	}

	public static <T> T get(String url,Map headMap,Class<T> clazz) throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		try {
			//设置head 参数
			if (headMap != null && headMap.size() > 0) {
				Set<String> headerSet = headMap.keySet();
				for (String key : headerSet) {
					httpGet.addHeader(key, headMap.get(key).toString());
				}
			}
			httpGet.setURI(new URI(httpGet.getURI().toString()));
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			if (entity != null) {
				entity.consumeContent();
			}
			return JSON.parseObject(body, clazz);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpGet.abort();
		}
	}

	public static String post(String url, List<NameValuePair> params)
			throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			return body;
		} catch (Exception e) {
			throw new Exception();
		} finally {
			post.abort();
		}
	}

	public static String postByBody(String url, JSONObject json)
			throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		try {
			post.setHeader("Content-Type", "application/json");
			post.setURI(new URI(post.getURI().toString()));
			StringEntity se = new StringEntity(json.toString(), "utf-8");
			post.setEntity(se);
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String str = EntityUtils.toString(entity, "utf-8");
			return str;
		} catch (Exception e) {
			throw new Exception();
		} finally {
			post.abort();
		}
	}

	public static String postByJson(String url, String data) throws UnsupportedEncodingException {
		String body = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(data, "utf-8");
		entity.setContentType("application/json");
		// entity.setContentEncoding("utf-8");
		httpPost.setEntity(entity);

		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity respEntity = response.getEntity();
				body = EntityUtils.toString(respEntity);
				if (respEntity != null) {
					respEntity.consumeContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	public static void postByJson1(String url, String data) throws UnsupportedEncodingException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(data, "utf-8");
		entity.setContentType("application/json");
		// entity.setContentEncoding("utf-8");
		httpPost.setEntity(entity);

		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		try {
			httpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get(String url) throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(50000)
					.setConnectionRequestTimeout(10000).setSocketTimeout(50000).build();
			httpGet.setConfig(requestConfig);
			httpGet.setURI(new URI(httpGet.getURI().toString()));
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			if (null != entity) {
				entity.consumeContent();
			}
			return body;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpGet.abort();
		}
	}


    public static String doPost(String reqUrl, Map <String, String> parameters, boolean encode) throws IOException {
		HttpURLConnection url_con = null;
		StringBuilder sb = new StringBuilder();
		String vchartset = "UTF-8";
		try {
			StringBuffer params = new StringBuffer();
			if(parameters!=null){
				for (Iterator <?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
					Map.Entry<?, ?> element = (Map.Entry<?, ?>) iter.next();
					params.append(element.getKey().toString());
					params.append("=");
					if(encode){
						params.append(URLEncoder.encode(element.getValue().toString(), vchartset));
					}else{
						params.append(element.getValue().toString());
					}
					params.append("&");
				}

				if (params.length() > 0) {
					params = params.deleteCharAt(params.length() - 1);
				}
			}

			URL url = new URL(reqUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			url_con.setConnectTimeout(5000);
			url_con.setReadTimeout(30000);
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			int code = url_con.getResponseCode();
			if (code != 200) {
				throw new IOException("http request error, code=" + code);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(url_con.getInputStream(),"utf-8"));
			String s = in.readLine();
			while (s != null) {
				sb.append(s);
				s = in.readLine();
			}
		} catch (IOException e) {
			//log.error("��[" + reqUrl + "]ͨ�Ź����з����쳣,��ջ��Ϣ����", e);
			throw e;
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		//log.debug("��[" + reqUrl + "]ͨ�Ź��� ������"+parameters+"����Ӧֵ " + (sb.toString()));
		return sb.toString();
	}


}
