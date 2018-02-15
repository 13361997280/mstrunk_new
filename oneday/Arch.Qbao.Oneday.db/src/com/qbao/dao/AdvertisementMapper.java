package com.qbao.dao;

import com.qbao.dto.Advertisement;

import java.util.List;

public interface AdvertisementMapper {
    List<Advertisement> selectRelaseStatus();
}