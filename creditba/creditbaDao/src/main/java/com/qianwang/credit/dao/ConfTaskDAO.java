package com.qianwang.credit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qianwang.credit.util.page.Page;
import com.qianwang.credit.dao.BaseBizDAO;
import com.qianwang.credit.entity.ConfTask;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfTaskDAO extends BaseBizDAO<ConfTask,Integer> {

	public List<ConfTask> pageQuery(Page page, @Param("id") Integer id, @Param("taskId") Integer taskId, @Param("addScore") Double addScore, @Param("subScore") Double subScore, @Param("status") Integer status, @Param("createTimeStart") Date createTimeStart, @Param("createTimeEnd") Date createTimeEnd, @Param("updateTimeStart") Date updateTimeStart, @Param("updateTimeEnd") Date updateTimeEnd, @Param("keyword") String keyword 
, @Param("orderBy") String orderBy, @Param("asc") Boolean asc);

	public List<ConfTask> queryList(Map<String,Object> params);

	public Long queryCount(Map<String,Object> params);

}
