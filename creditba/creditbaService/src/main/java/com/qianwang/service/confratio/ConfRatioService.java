package com.qianwang.service.confratio;

import com.qianwang.dao.domain.advertisement.ConfRatio;

import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/20
 */
public interface ConfRatioService {

    List<ConfRatio> getAll();

    int addConfRatio(List<ConfRatio> ratios,String userName);

    List<ConfRatio> getDis();

    List<ConfRatio> getListBylistName(ConfRatio record);

    int upConfRatio(ConfRatio ratios,String userName);

}
