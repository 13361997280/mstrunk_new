package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * @author: hz
 * @date:
 */
public class Crawler {

	/**
	 * HttpClient get responType : application/xml / application/json
	 * **/
	public boolean return200OK(String url, String responType, int timeOutMax) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("accept", responType);
		HttpEntity respEntity = null;
		// 请求超时
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeOutMax);
		// 读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				timeOutMax);
		try {
			HttpResponse resp = httpClient.execute(httpGet);
			respEntity = resp.getEntity();
			if (resp.getStatusLine().getStatusCode() == 200) {
				return true;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (respEntity != null) {
				try {
					EntityUtils.consume(respEntity);
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
			httpClient.getConnectionManager().shutdown();
		}
		return false;
	}

	/**
	 * HttpClient get responType : application/xml / application/json
	 * **/
	public StringBuffer get(String url, String responType, int timeOutMax) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("accept", responType);
		HttpEntity respEntity = null;
		// 请求超时
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, timeOutMax);
		// 读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				timeOutMax);
		try {
			HttpResponse resp = httpClient.execute(httpGet);
			respEntity = resp.getEntity();
			if (resp.getStatusLine().getStatusCode() == 200) {
				InputStream is = respEntity.getContent();
				return new StringBuffer().append(new String(
						readStream(is, 2048)));
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (respEntity != null) {
				try {
					EntityUtils.consume(respEntity);
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
			httpClient.getConnectionManager().shutdown();
		}
		return new StringBuffer();
	}

	private byte[] readStream(InputStream is, int bufLength) throws IOException {
		byte[] rt = null;
		byte[] buf = new byte[bufLength];
		int readCount = is.read(buf);
		while (readCount > 0) {
			if (rt == null) {
				rt = new byte[readCount];
				System.arraycopy(buf, 0, rt, 0, readCount);
			} else {
				byte[] tmp = new byte[rt.length + readCount];
				System.arraycopy(rt, 0, tmp, 0, rt.length);
				System.arraycopy(buf, 0, tmp, rt.length, readCount);
				rt = tmp;
			}
			readCount = is.read(buf);
		}
		return rt;
	}

	/**
	 * URLConnection get 可以解决urlencode后乱码问题
	 * **/
	public StringBuffer urlGet(String urlStr, String responType, int timeOutMax) {
		StringBuffer sb = new StringBuffer();
		try {
			// logger.info(urlStr);
			URL getUrl = new URL(urlStr);
			URLConnection connection = getUrl.openConnection();
			connection.setRequestProperty("accept", responType);
			connection.setReadTimeout(timeOutMax);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			while ((lines = reader.readLine()) != null) {
				sb.append(lines);
			}
			reader.close();
		} catch (Exception e) {
			// logger.error(e);
		}

		return sb;
	}

	public static void main(String[] args) {
		/*long startTime = System.currentTimeMillis();
		Crawler json = new Crawler();
		String url = "http://www.dooioo.com/ershoufang?kw=上南花苑&auto=false";
		String html = new String(json.get(url, "application/json", 6000));
		System.out.println(html);*/
		
		String url = "http://localhost:18181/solr/estate/select?q=*%3A*&rows=1111&wt=json&indent=true";
		Crawler http = new Crawler();
		System.out.println(http.get(url, "text/html", 1000));
	
		if(http.return200OK(url, "text/html", 6000)){
		
		System.out.println("ok");}

	}

}
