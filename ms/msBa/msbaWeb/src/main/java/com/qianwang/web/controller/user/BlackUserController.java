package com.qianwang.web.controller.user;

import com.qianwang.dao.domain.user.UnusualUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianwang.service.user.BlackUserService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/blackUser/**")
public class BlackUserController extends BaseController {
    protected static final Log LOG = LogFactory.getLog(BlackUserController.class);

    private static final CloseableHttpClient httpClient;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final int CONNECT_TIMEOUT = 60000;
    public static final String HTTP_CHARACTER_SET = "UTF-8";

    static{
        RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    @Autowired
    private BlackUserService blackUserService;

    @RequestMapping(value = "/getBlackUser")
    @ResponseBody
    public AjaxResult getUserSource(String startDate, String endDate, String userName, Integer lockReasonType) {
        return AjaxResult.successResult(blackUserService.getBlackUser(startDate, endDate, userName, lockReasonType));
    }

    @RequestMapping(value = "/getBlackUserCnt")
    @ResponseBody
    public AjaxResult getUserSourceCnt(String startDate, String endDate, String userName, Integer lockReasonType) {
        return AjaxResult
                .successResult(blackUserService.getBlackUser(startDate, endDate, userName, lockReasonType).size());
    }

    @RequestMapping(value = "/getBlackUserDayGroup")
    @ResponseBody
    public AjaxResult getUserSourceGroup(String startDate, String endDate, String userName, Integer lockReasonType) {
        return AjaxResult
                .successResult(blackUserService.getBlackUserDayGroup(startDate, endDate, userName, lockReasonType));
    }

    @RequestMapping(value = "/getBlackUserHourGroup")
    @ResponseBody
    public AjaxResult getUserSourceHourGroup(String startDate, String endDate, String userName,
                                             Integer lockReasonType) {
        return AjaxResult
                .successResult(blackUserService.getBlackUserHourGroup(startDate, endDate, userName, lockReasonType));
    }

    @RequestMapping(value = "/getBlackUserReason")
    @ResponseBody
    public AjaxResult getBlackUserReason() {
        return AjaxResult
                .successResult(blackUserService.getBlackUserReason());
    }

    @RequestMapping(value = "/getUnBlackUser")
    @ResponseBody
    public AjaxResult getUnUserSource(String startDate, String endDate, String userName) {
        return AjaxResult.successResult(blackUserService.getUnBlackUser(startDate, endDate, userName));
    }

    @RequestMapping(value = "/getUnBlackUserCnt")
    @ResponseBody
    public AjaxResult getUnUserSourceCnt(String startDate, String endDate, String userName) {
        return AjaxResult
                .successResult(blackUserService.getUnBlackUser(startDate, endDate, userName).size());
    }

    @RequestMapping(value = "/getUnBlackUserDayGroup")
    @ResponseBody
    public AjaxResult getUnUserSourceGroup(String startDate, String endDate, String userName) {
        return AjaxResult
                .successResult(blackUserService.getUnBlackUserDayGroup(startDate, endDate, userName));
    }

    @RequestMapping(value = "/getUnBlackUserHourGroup")
    @ResponseBody
    public AjaxResult getUnUserSourceHourGroup(String startDate, String endDate, String userName) {
        return AjaxResult
                .successResult(blackUserService.getUnBlackUserHourGroup(startDate, endDate, userName));
    }

    @RequestMapping(value = "/getUnBlackUserByTime")
    @ResponseBody
    public AjaxResult getUnBlackUserByTime(String startDate, String endDate, String time) {
        return AjaxResult.successResult(blackUserService.getUnBlackUserByTime(time));
    }

    @RequestMapping(value = "/getUnBlackUserByTimeCnt")
    @ResponseBody
    public AjaxResult getUnBlackUserByTimeCnt(String startDate, String endDate, String time) {
        return AjaxResult
                .successResult(blackUserService.getUnBlackUserByTime(time).size());
    }

    @RequestMapping(value = "/getUnBlackUserByDay")
    @ResponseBody
    public AjaxResult getUnBlackUserByDay(String startDate, String endDate, String time) {
        return AjaxResult.successResult(blackUserService.getUnBlackUserByDay(time));
    }

    @RequestMapping(value = "/getUnBlackUserByDayCnt")
    @ResponseBody
    public AjaxResult getUnBlackUserByDayCnt(String startDate, String endDate, String time) {
        return AjaxResult
                .successResult(blackUserService.getUnBlackUserByDay(time).size());
    }

    @RequestMapping(value = "/getYiChangUser")
    @ResponseBody
    public AjaxResult getYiChangUser(String startDate, String endDate, String userName, Integer lockReasonType) {
        return AjaxResult.successResult(blackUserService.getYiChangUser(startDate, endDate, userName, lockReasonType));
    }

    @RequestMapping(value = "/getYiChangUserCnt")
    @ResponseBody
    public AjaxResult getYiChangUserCnt(String startDate, String endDate, String userName, Integer lockReasonType) {
        return AjaxResult
                .successResult(blackUserService.getYiChangUser(startDate, endDate, userName, lockReasonType).size());
    }

    @RequestMapping(value = "/updateYiChangUser")
    @ResponseBody
    public AjaxResult updateYiChangUser(String orderInfo, Integer type) {
//        String tjson= "[{'orderId':1234,'type':1},{'orderId':12345,'type':2},{'orderId':12345,'type':1}]";
        JSONArray jsonArray = JSONArray.fromObject(orderInfo);
        System.out.println(jsonArray);
        List<UnusualUser> userList1 = (List<UnusualUser>) JSONArray.toCollection(jsonArray, UnusualUser.class);
        int tss=0;
        Map<String, String> params = new LinkedHashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<userList1.size();i++)
         {
             UnusualUser TS= userList1.get(i);
             String orderId =TS.getOrderId();
             Integer typeOne =(TS.getDealResult());
             String userId =TS.getUserId();
             params.put("orderId", orderId);
             params.put("userId", userId);
             String msg ="你的订单有风险";
             int httpType = 1;
             if (typeOne==2) {
              //调用2的接口
                 httpType = 2;
                 params.put("reason", msg);
                 String CHECK_URL="/api/v1/dc/risk/close.html";
                 String messageResult = doGetResult(CHECK_URL, params,"UTF-8",httpType);
                 System.out.println("状态"+ messageResult);
                 if(null!=messageResult){
                     if(messageResult.equals("true")){
                         blackUserService.updateYiChangUser(orderId, typeOne);
                     }
                 }
             }else if(typeOne==0){
               //调用0的接口
                 httpType = 0;
                 String CHECK_AUDIT_URL="/api/v1/dc/examineAudit.html";
                 String messageResult = doGetResult(CHECK_AUDIT_URL, params,"UTF-8",httpType);
                 System.out.println("调用状态"+ messageResult);
                 if(null!=messageResult){
                     if(messageResult.equals("true")){
                         blackUserService.updateYiChangUser(orderId, typeOne);
                     }
                 }

             }
        }
        System.out.println("返回结果"+tss);
        return AjaxResult.successResult(tss);
    }

    /**
     * HTTP doGetResult 获取内容
     * @param url  请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset    编码格式
     * @return    页面内容
     */
    public String doGetResult(String url, Map<String, String> params, String charset ,int htype ){
        if(StringUtils.isBlank(url)){
            return null;
        }
        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for(Map.Entry<String,String> entry : params.entrySet()){
                String value = entry.getValue();
                if(value != null){
                    pairs.add(new BasicNameValuePair(entry.getKey(),value));
                }
            }
            try {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//		String thost = "192.168.162.146";
        String thost = "http://oc.qbao.com";
        String allurl = thost + url;
        HttpGet httpGet = new HttpGet(allurl);
        HttpPost httpPost = new HttpPost(allurl);

        try {
            CloseableHttpResponse response=null;
            if(htype == 0){
                LOG.info("get请求"+httpGet);
                response = httpClient.execute(httpGet);
            }else{
                LOG.info("psst请求"+httpPost);
                response = httpClient.execute(httpPost);
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                return "访问失败";
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null){
                String result2 = EntityUtils.toString(entity, "utf-8");
                LOG.info("resp返回内容"+result2);
                if(!"".equals(result2) && htype==0) {
                    JSONObject obj = JSONObject.fromObject(result2);
                    LOG.info("返回success类型"+obj);
                    result = obj.get("success").toString();
                }
                if(!"".equals(result2) && htype==2) {
                    if (result2 != null && result2.contains("false")) {
                        result = "false";
                    }else{
                        result = "true";
                    }
                }
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            LOG.error("调用修改订单状态接口错误:", e);
        }
        return null;
    }
}
