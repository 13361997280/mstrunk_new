package com.qianwang.dao.domain.assistant;

public class AuthoritiesGroup {
    private Integer id;

    private String name;

    private String resouceIds;

    private String resouceNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResouceIds() {
        return resouceIds;
    }

    public void setResouceIds(String resouceIds) {
        this.resouceIds = resouceIds;
    }

    public String getResouceNames() {
        return resouceNames;
    }

    public void setResouceNames(String resouceNames) {
        this.resouceNames = resouceNames;
    }
}