package com.qianwang.credit.dao;

import java.util.List;
import java.util.Map;

import com.qianwang.credit.entity.ConfBus;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfBusDAO extends BaseBizDAO<ConfBus,Integer> {

	public List<ConfBus> queryList(Map<String,Object> params);

	public Long queryCount(Map<String,Object> params);

}
