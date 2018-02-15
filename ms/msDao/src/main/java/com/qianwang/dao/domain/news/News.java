package com.qianwang.dao.domain.news;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Date;

public class News {
    private Integer id;

    private String title;

    private String subTitle;

    private Integer status;

    private Integer channel;

    private String imageUrl;

    private String newsContent;

    private String outLink;

    private Date createTime;
    private Date updateTime;
    private String updateTimeStr;

    private Boolean isRelease;

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public Boolean getRelease() {
        return isRelease;
    }

    public void setRelease(Boolean release) {
        isRelease = release;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOutLink() {
        return outLink;
    }

    public void setOutLink(String outLink) {
        this.outLink = outLink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle == null ? null : subTitle.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent == null ? null : newsContent.trim();
    }

    public void addInvoke() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("请输入新闻标题!");
        } else {
            if (this.title.trim().length() > 20) {
                throw new IllegalArgumentException("新闻标题长于20字!");
            }
        }


        if (this.subTitle == null || this.subTitle.isEmpty()) {
            throw new IllegalArgumentException("请输入新闻标签");
        } else {
            if (this.subTitle.trim().length() > 20) {
                throw new IllegalArgumentException("新闻标签长于20字!");
            }
        }

        if (this.outLink == null || this.outLink.isEmpty()) {
            throw new IllegalArgumentException("请输入新闻外链");
        }else {
            if (this.outLink.trim().length() > 200) {
                throw new IllegalArgumentException("新闻链接长于200字符!");
            }
        }

        if (this.imageUrl == null || this.imageUrl.isEmpty()) {
            throw new IllegalArgumentException("请上传图片!");
        }

        if (this.newsContent == null || this.newsContent.isEmpty()) {
            throw new IllegalArgumentException("请输入新闻内容!");
        }
    }

    /**
     * 入库之前转特殊字符
     */
    public void xssFilter(){
        this.title = StringEscapeUtils.escapeHtml(this.title);
        this.subTitle = StringEscapeUtils.escapeHtml(this.subTitle);
        this.imageUrl = StringEscapeUtils.escapeHtml(this.imageUrl);
        this.outLink = StringEscapeUtils.escapeHtml(this.outLink);
        this.newsContent = StringEscapeUtils.escapeHtml(this.newsContent);

        this.title = StringEscapeUtils.escapeJavaScript(this.title);
        this.subTitle = StringEscapeUtils.escapeJavaScript(this.subTitle);
        this.imageUrl = StringEscapeUtils.escapeJavaScript(this.imageUrl);
        this.outLink = StringEscapeUtils.escapeJavaScript(this.outLink);
        this.newsContent = StringEscapeUtils.escapeJavaScript(this.newsContent);
    }

}