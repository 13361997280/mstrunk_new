package com.qianwang.dao.domain.assistant;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

public class AssPromotion {
    private Integer id;

    private String title;

    private String description;

    private Byte type;

    private Boolean status;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private String iconUrl;

    private String activityUrl;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl == null ? null : iconUrl.trim();
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl == null ? null : activityUrl.trim();
    }

    @Override
    public String toString() {
        return "AssPromotion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", iconUrl='" + iconUrl + '\'' +
                ", activityUrl='" + activityUrl + '\'' +
                '}';
    }
    
    public void addInvoke() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("请输入标题!");
        } else {
            if (this.title.trim().length() > 120) {
                throw new IllegalArgumentException("标题长于120字!");
            }
        }
        if (this.description == null || this.description.isEmpty()) {
            throw new IllegalArgumentException("请输入描述");
        } else {
            if (this.description.trim().length() > 250) {
                throw new IllegalArgumentException("描述长于250字!");
            }
        }
        if (this.activityUrl == null || this.activityUrl.isEmpty()) {
            throw new IllegalArgumentException("请输入活动链接");
        }else {
            if (this.activityUrl.trim().length() > 200) {
                throw new IllegalArgumentException("活动链接长于200字符!");
            }
        }
        if (this.iconUrl == null || this.iconUrl.isEmpty()) {
            throw new IllegalArgumentException("请上传图片!");
        }
        if (this.startTime == null || this.endTime == null) {
            throw new IllegalArgumentException("请输入开始结束时间!");
        }
        if (this.endTime.getTime() < this.startTime.getTime()) {
            throw new IllegalArgumentException("结束时间不能小于开始时间!");
        }

    }

    /**
     * 入库之前转特殊字符
     */
    public void xssFilter(){
        this.title = StringEscapeUtils.escapeHtml(this.title);
        this.description = StringEscapeUtils.escapeHtml(this.description);
        this.iconUrl = StringEscapeUtils.escapeHtml(this.iconUrl);
        this.activityUrl = StringEscapeUtils.escapeHtml(this.activityUrl);

        this.title = StringEscapeUtils.escapeJavaScript(this.title);
        this.description = StringEscapeUtils.escapeJavaScript(this.description);
        this.iconUrl = StringEscapeUtils.escapeJavaScript(this.iconUrl);
        this.activityUrl = StringEscapeUtils.escapeJavaScript(this.activityUrl);
    }
}