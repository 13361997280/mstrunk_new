package com.qianwang.mapper.sysconfig;

import com.qianwang.dao.domain.advertisement.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by YcY_YcY on 2017/11/16
 */
public interface SysconfigMapper {
    List<SysConfig> selectSysConfig();

    int insertConfig(SysConfig config);

    int updateConfig(SysConfig config);

    int delConfig(SysConfig config);

    SysConfig selectSysConfigByKey(@Param("sysKey") String sysKey);
}
