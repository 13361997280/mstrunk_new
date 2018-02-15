package com.qbao.search.util;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class HttpClientUtil {  
    private final static String charset = "UTF-8";  
    private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000)
            .setConnectTimeout(15000)  
            .setConnectionRequestTimeout(15000)  
            .build();  
      
    private static HttpClientUtil instance = null;  
    private HttpClientUtil(){}  
    public static HttpClientUtil getInstance(){  
        if (instance == null) {  
            instance = new HttpClientUtil();  
        }  
        return instance;  
    }  
      
    /** 
     * 发送 post请求 
     * @param httpUrl 地址 
     */  
    public String sendHttpPost(String httpUrl) {  
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        return sendHttpPost(httpPost);  
    }  
      
    /** 
     * 发送 post请求 
     * @param httpUrl 地址 
     * @param params 参数(格式:key1=value1&key2=value2) 
     */  
    public String sendHttpPost(String httpUrl, String params) {  
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {  
            //设置参数  
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");   
            httpPost.setEntity(stringEntity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sendHttpPost(httpPost);  
    }  
       
    /** 
     * 发送 post请求 
     * @param httpUrl 地址 
     * @param maps 参数 
     */  
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {  
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded;charset="+charset);  
        httpPost.setHeader("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.");  
        // 创建参数队列    
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {  
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }  
        try {  
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sendHttpPost(httpPost);  
    }  
      
      
    /** 
     * 发送 post请求（带文件） 
     * @param httpUrl 地址 
     * @param maps 参数 
     * @param fileLists 附件 
     */  
    public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {  
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();  
        for (String key : maps.keySet()) {  
            meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
        }  
        for(File file : fileLists) {  
            FileBody fileBody = new FileBody(file);  
            meBuilder.addPart("files", fileBody);  
        }  
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);  
        return sendHttpPost(httpPost);  
    }  
      
    /** 
     * 发送Post请求 
     * @param httpPost 
     * @return 
     */  
    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;  
        try {  
            // 创建默认的httpClient实例.  
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);  
            // 执行请求  
            response = httpClient.execute(httpPost);  
            entity = response.getEntity();  
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return responseContent;  
    }  
  
    /** 
     * 发送 get请求 
     * @param httpUrl 
     */  
    public String sendHttpGet(String httpUrl) {  
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet);  
    }  
  
    /** 
     * 发送 get请求Https 
     * @param httpUrl 
     */  
    public String sendHttpsGet(String httpUrl) {  
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet);  
    }  
      
    /** 
     * 发送Get请求 
     * @param httpGet
     * @return 
     */  
    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;  
        try {  
            // 创建默认的httpClient实例.  
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);  
            // 执行请求  
            response = httpClient.execute(httpGet);  
            entity = response.getEntity();  
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return responseContent;  
    }  
      
    /** 
     * 发送Get请求Https 
     * @param httpGet
     * @return 
     */  
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;  
        try {  
            // 创建默认的httpClient实例.  
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));  
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);  
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);  
            // 执行请求  
            response = httpClient.execute(httpGet);  
            entity = response.getEntity();  
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                // 关闭连接,释放资源  
                if (response != null) {  
                    response.close();  
                }  
                if (httpClient != null) {  
                    httpClient.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return responseContent;  
    }  
      
    /** 
     * 利用httpClient获取页面 
     * @param url 
     * @return 
     */  
     public static String getPage(String url){  
         String result="";  
        HttpClient httpClient = new HttpClient();  
        GetMethod getMethod = new GetMethod(url+"?date=" + new Date().getTime());//加时间戳，防止页面缓存
         getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");


        try {  
            int statusCode = httpClient.executeMethod(getMethod);
            httpClient.setTimeout(5000);
            httpClient.setConnectionTimeout(5000);

            if (statusCode != HttpStatus.SC_OK) {  
                System.err.println("Method failed: "+ getMethod.getStatusLine());  
            }  
              
            // 读取内容  
            //byte[] responseBody = getMethod.getResponseBody();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream()));    
            StringBuffer stringBuffer = new StringBuffer();    
            String str = "";    
            while((str = reader.readLine())!=null){    
                stringBuffer.append(str);    
            }    
            // 处理内容  
            result = stringBuffer.toString();  
        } catch (Exception e) {  
            System.err.println("页面无法访问");  
        }  
        getMethod.releaseConnection();  
        return result;  
  }
    /**
     * 下载图片到本地
     * @param picUrl 图片Url
     * @param localPath 本地保存图片地址
     * @return
     */
    public String downloadPic(String picUrl,String localPath){
        String filePath = null;
        String url = null;
        try {
            URL httpurl = new URL(picUrl);
            String fileName = getFileNameFromUrl(picUrl);
            filePath = localPath + fileName;
            File f = new File(filePath);
            //FileUtils.copyURLToFile(httpurl, f);
            //Function fun = new Function();
            //url = filePath.replace("/www/web/imgs", fun.getProValue("IMG_PATH"));
        } catch (Exception e) {
            //logger.info(e);
            return null;
        }
        return url;
    }
    /**
     * 替换内容中图片地址为本地地址
     * @param content html内容
     * @param pic_dir 本地地址文件路径
     * @return html内容
     */
    public static String replaceForNews(String content,String pic_dir){
        String str = content;
        String cont = content;
        while (true) {
            int i = str.indexOf("src=\"");
            if (i != -1) {
                str = str.substring(i+5, str.length());
                int j = str.indexOf("\"");
                String pic_url = str.substring(0, j);
                //下载图片到本地并返回图片地址
                String pic_path = "";//fun.downloadPicForNews(pic_url,pic_dir);
                if(StringUtils.isNotEmpty(pic_url) && StringUtils.isNotEmpty(pic_path)){
                    cont = cont.replace(pic_url, pic_path);
                    str = str.substring(j,str.length());
                }
            } else{
                break;
            }
        }
        return cont;
    }
    /**
     * 下载图片到本地
     * @param picUrl 图片Url
     * @param localPath 本地保存图片地址
     * @return
     */
    public String downloadPicForNews(String picUrl,String localPath){
        String filePath = "";
        String url = "";
        try {
            URL httpurl = new URL(picUrl);
            HttpURLConnection urlcon = (HttpURLConnection) httpurl.openConnection();
            urlcon.setReadTimeout(3000);
            urlcon.setConnectTimeout(3000);
            int state = urlcon.getResponseCode(); //图片状态
            if(state == 200){
                String fileName = getFileNameFromUrl(picUrl);
                filePath = localPath + fileName;
                File f = new File(filePath);
                FileUtils.copyURLToFile(httpurl, f);
                //Function fun = new Function();
                //url = filePath.replace("/www/web/imgs", fun.getProValue("IMG_PATH"));
            }
        } catch (Exception e) {
            //logger.info(e);
            return null;
        }
        return url;
    }
    /**
     * 根据url获取文件名
     * @param url
     * @return 文件名
     */
    public static String getFileNameFromUrl(String url){
        //获取后缀
        String sux = url.substring(url.lastIndexOf("."));
        if(sux.length() > 4){
            sux = ".jpg";
        }
        int i = (int)(Math.random()*1000);
        //随机时间戳文件名称
        String name = new Long(System.currentTimeMillis()).toString()+ i + sux;
        return name;
    }
} 