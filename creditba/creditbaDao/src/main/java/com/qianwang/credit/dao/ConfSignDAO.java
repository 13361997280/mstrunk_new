package com.qianwang.credit.dao;

import java.util.List;
import java.util.Map;

import com.qianwang.credit.entity.ConfSign;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfSignDAO extends BaseBizDAO<ConfSign,Integer> {

	public List<ConfSign> queryList(Map<String,Object> params);

	public Long queryCount(Map<String,Object> params);

	public ConfSign getCurrentSign();

}
