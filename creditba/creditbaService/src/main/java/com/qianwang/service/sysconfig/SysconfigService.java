package com.qianwang.service.sysconfig;

import com.qianwang.dao.domain.advertisement.SysConfig;
import com.qianwang.service.vo.Dict;

import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/16
 */
public interface SysconfigService {
    List<SysConfig> getSysconfig();


    String getKey(String key);

    List<String> getNameListByPerfix(String key);

    int addSysconfig(SysConfig config ,String userName);


    int  editSysconfig(SysConfig config,String userName);


    List<Dict> getDicts(String sysKey);

    int  delSysconfig(SysConfig config);
}
