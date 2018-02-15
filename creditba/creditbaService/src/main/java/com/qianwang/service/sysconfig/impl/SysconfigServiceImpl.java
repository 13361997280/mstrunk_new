package com.qianwang.service.sysconfig.impl;

import com.alibaba.fastjson.JSON;
import com.qianwang.constant.RedisConst;
import com.qianwang.dao.domain.advertisement.SysConfig;
import com.qianwang.mapper.sysconfig.SysconfigMapper;
import com.qianwang.service.sysconfig.SysconfigService;
import com.qianwang.service.vo.Dict;
import com.qianwang.util.Redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by YcY_YcY on 2017/11/16
 */
@Service
public class SysconfigServiceImpl implements SysconfigService {

    protected static final Logger LOG = LoggerFactory.getLogger(SysconfigServiceImpl.class);

    @Autowired
    private SysconfigMapper sysconfigMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<SysConfig> getSysconfig() {
        return sysconfigMapper.selectSysConfig();
    }


    void loadConfig() {
        List<SysConfig> sysConfigs = sysconfigMapper.selectSysConfig();
        for (SysConfig c : sysConfigs) {
            redisUtil.set(String.format(RedisConst.CREDIT_SYS_CONFIG.key, c.getSysKey()), c.getSysValue(), RedisConst.CREDIT_SYS_CONFIG.expired);
        }
    }

    public String getKey(String key) {
        String value = redisUtil.get(String.format(RedisConst.CREDIT_SYS_CONFIG.key, key));
        if (null == value) {
            loadConfig();
            value = redisUtil.get(String.format(RedisConst.CREDIT_SYS_CONFIG.key, key));
        }
        return value;
    }

    public List<String> getNameListByPerfix(String pre_str) {
        Set<String> set = redisUtil.getKeys(String.format(RedisConst.CREDIT_SYS_CONFIG.key, pre_str) +"*");
        if(set.size() == 0){
            loadConfig();
            set = redisUtil.getKeys(pre_str +"*");
        }
        Iterator<String> it = set.iterator();
        List<String> accountList=new ArrayList<String>();
        while(it.hasNext()){
            String keyStr = it.next();
            accountList.add(getKey(keyStr));
        }
        return accountList;
    }

    @Override
    public int addSysconfig(SysConfig config, String userName) {
        int flag = 0;
        try {
            config.setOperator(userName);
            flag = sysconfigMapper.insertConfig(config);
            loadConfig();
        } catch (Exception e) {
            LOG.error("SysconfigServiceImpl-addSysconfig():e,{}", e);
        }
        return flag;
    }

    @Override
    public int editSysconfig(SysConfig config, String userName) {
        int flag = 0;
        try {
            config.setOperator(userName);
            flag = sysconfigMapper.updateConfig(config);
            loadConfig();
        } catch (Exception e) {
            LOG.error("SysconfigServiceImpl-editSysconfig():e,{}", e);
        }
        return flag;
    }
    @Override
    public List<Dict> getDicts(String sysKey) {
        List<Dict> dicts = null;
        try {
            SysConfig sysConfig = sysconfigMapper.selectSysConfigByKey(sysKey);
            dicts =JSON.parseArray(sysConfig.getSysValue(),Dict.class);
        } catch (Exception e) {
            LOG.error("SysconfigServiceImpl-getDicts():e,{}", e);
        }
        return dicts;
    }



    public int delSysconfig(SysConfig config) {
        int flag = 0;
        try {
            flag = sysconfigMapper.delConfig(config);
            redisUtil.del(String.format(RedisConst.CREDIT_SYS_CONFIG.key, config.getSysKey()));
        } catch (Exception e) {
            LOG.error("SysconfigServiceImpl-delConfig():e,{}", e);
        }
        return flag;
    }

}
