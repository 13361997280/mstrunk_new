package com.qianwang.dao.domain.advertisement;

import java.util.Date;

public class Advertisement {
    private Integer id;

    private String title;

    private Date startTime;

    private Date endTime;

    private String position;

    private Integer status;

    private String custIds;

    private String custName;

    private Integer feeType;

    private String keyword;

    private Date createStmp;

    private Date updateStmp;

    private Integer deleteFlag;

    private String indexImage;

    private String newsImage;

    private String description;

    private String activityUrl;

    private String operator;

    private Integer release;

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
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustIds() {
        return custIds;
    }

    public void setCustIds(String custIds) {
        this.custIds = custIds;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCreateStmp() {
        return createStmp;
    }

    public void setCreateStmp(Date createStmp) {
        this.createStmp = createStmp;
    }

    public Date getUpdateStmp() {
        return updateStmp;
    }

    public void setUpdateStmp(Date updateStmp) {
        this.updateStmp = updateStmp;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getIndexImage() {
        return indexImage;
    }

    public void setIndexImage(String indexImage) {
        this.indexImage = indexImage;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getOperator() {
        return operator;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getRelease() {
        return release;
    }

    public void setRelease(Integer release) {
        this.release = release;
    }

    public void validate() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("请输入标题!");
        } else {
            if (this.title.trim().length() > 50) {
                throw new IllegalArgumentException("标题长于50字!");
            }
        }

        if (this.position == null || this.position.isEmpty()) {
            throw new IllegalArgumentException("请选择展示位置!");
        }
        if (this.custIds == null || this.custIds.isEmpty()) {
            throw new IllegalArgumentException("请选择推广客户!");
        }
        if (this.keyword == null || this.keyword.isEmpty()) {
            throw new IllegalArgumentException("请输入推广关键字!");
        }
        if (this.feeType == null) {
            throw new IllegalArgumentException("请选择结算方式!");
        }
        if (this.position.contains("0")
                &&(this.indexImage == null || this.indexImage.isEmpty())) {
            throw new IllegalArgumentException("请上传首页广告图片!");
        }
        if (this.position.contains("1")&&this.position.contains("2")
                &&(this.newsImage == null || this.newsImage.isEmpty())) {
            throw new IllegalArgumentException("请上传新闻广告图片!");
        }

        if (this.activityUrl == null || this.activityUrl.isEmpty()) {
            throw new IllegalArgumentException("请输入超链");
        }else {
            if (this.activityUrl.trim().length() > 200) {
                throw new IllegalArgumentException("超链长于200字符!");
            }
        }

        if (this.startTime == null || this.endTime == null) {
            throw new IllegalArgumentException("请输入开始结束时间!");
        }
        if (this.endTime.getTime() < this.startTime.getTime()) {
            throw new IllegalArgumentException("结束时间不能小于开始时间!");
        }
        long now = new Date().getTime();
        if (null!=this.release&&this.release==1&&this.status==1&&(now>this.endTime.getTime() || now<this.startTime.getTime())) {
            throw new IllegalArgumentException("不在展示时间范围内的不能上架!");
        }

    }
}