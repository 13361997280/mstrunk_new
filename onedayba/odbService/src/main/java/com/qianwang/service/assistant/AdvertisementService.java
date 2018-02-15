package com.qianwang.service.assistant;

import com.qianwang.dao.domain.advertisement.Advertisement;

import java.util.List;

/**
 * Created by chenghaijiang on 2017/10/23.
 */
public interface AdvertisementService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Advertisement record);

    Advertisement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Advertisement record);

    List<Advertisement> selectByCondi();
}
