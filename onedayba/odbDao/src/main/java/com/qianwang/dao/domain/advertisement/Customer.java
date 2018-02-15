package com.qianwang.dao.domain.advertisement;

import java.util.Date;

public class Customer {
    private Integer id;

    private String custName;

    private String learder;

    private String address;

    private String phone;

    private Date createStmp;

    private Date updateStmp;

    private Byte deleteFlag;

    private Integer creator;

    private String creatorName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getLearder() {
        return learder;
    }

    public void setLearder(String learder) {
        this.learder = learder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}