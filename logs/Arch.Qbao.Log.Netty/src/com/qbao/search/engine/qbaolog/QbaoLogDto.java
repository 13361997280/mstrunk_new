package com.qbao.search.engine.qbaolog;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author song.j
 * @create 2017-07-11 16:16:52
 **/
public class QbaoLogDto {

    private String companyId;       //公司ID
    private String productId;       //产品ID
    private String pageId;          //页面ID
    private String marketId;        //活动ID
    private String channelId;       //渠道ID
    private String IP;
    private String client;
    private String appName;
    private String appVersion;
    private String windowId;
    private String joinCode;
    private String refer;
    private String sid;
    private String title;
    private String uid;
    private String uname;
    private String begin;
    private String end;
    private String event;
    private String stay;
    private String x;
    private String y;
    private String x_g;
    private String y_g;
    private String data;
    private Long stamp;
    private String stampDate;
    private String fid;

    private String logId;

    /**
     * 必填参数
     */
    public static final List<String> requiredData = Arrays.asList(new String[]
            {"companyId", "productId", "pageId"});


    /**
     * 所有参数list集合
     */
    public static final List<String> elmData = Arrays.asList(new String[]
            {"companyId", "productId", "pageId", "marketId", "channelId", "IP", "client",
                    "appName", "appVersion", "windowId", "joinCode", "refer", "sid", "title", "uid", "uname", "begin", "end", "event",
                    "stay", "x", "y", "data", "stamp", "stampDate", "fid", "x_g", "y_g"
            });


    public String getStampDate() {
        return stampDate;
    }

    public void setStampDate(String stampDate) {
        this.stampDate = stampDate;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getWindowId() {
        return windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStay() {
        return stay;
    }

    public void setStay(String stay) {
        this.stay = stay;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getX_g() {
        return x_g;
    }

    public void setX_g(String x_g) {
        this.x_g = x_g;
    }

    public String getY_g() {
        return y_g;
    }

    public void setY_g(String y_g) {
        this.y_g = y_g;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
