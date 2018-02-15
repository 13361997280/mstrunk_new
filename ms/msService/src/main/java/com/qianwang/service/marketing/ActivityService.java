package com.qianwang.service.marketing;

import com.qianwang.dao.domain.ActSend;

import java.util.Map;

/**
 * @author song.j
 * @create 2017-05-16 11:11:34
 **/
public interface ActivityService {

    Map getActivityByPage(int page, int size, int userId);

    int saveActivity(ActSend actSend);
}
