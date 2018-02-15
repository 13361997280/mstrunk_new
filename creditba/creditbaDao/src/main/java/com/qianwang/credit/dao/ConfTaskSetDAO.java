package com.qianwang.credit.dao;

import com.qianwang.credit.entity.ConfTaskSet;
import com.qianwang.credit.util.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfTaskSetDAO extends BaseBizDAO<ConfTaskSet,Integer> {

	public List<ConfTaskSet> pageQuery(Page page, @Param("id") Integer id, @Param("taskId") Integer taskId, @Param("addScore") Double addScore, @Param("subScore") Double subScore, @Param("status") Integer status, @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd, @Param("updateTimeStart") Date updateTimeStart, @Param("updateTimeEnd") Date updateTimeEnd, @Param("keyword") String keyword
			, @Param("orderBy") String orderBy, @Param("asc") Boolean asc);

	public List<ConfTaskSet> queryList(Map<String, Object> params);

	public Long queryCount(Map<String, Object> params);

	public List<ConfTaskSet> firstQuery();

}
