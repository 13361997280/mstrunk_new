package com.qianwang.credit.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 业务DAO基类
 * @author wangjg
 *
 * @param <T>
 * @param <ID>
 */
public interface BaseBizDAO<T, ID extends Serializable> {
	public void save(final T entity);

	public Long update(T entity);

	public void delete(ID id);
	
	public T findById(ID id);
	
	public List<T> queryList(Map<String, Object> params);
	
	public int queryTotal(Map<String, Object> map);
	
}
