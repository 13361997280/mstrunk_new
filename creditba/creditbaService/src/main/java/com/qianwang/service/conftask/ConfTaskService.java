package com.qianwang.service.conftask;

import java.util.Date;
import java.util.List;

import com.qianwang.credit.util.page.Page;

import com.qianwang.credit.entity.ConfTask;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfTaskService {

	public ConfTask getConfTask(Integer id);

	public void save(ConfTask confTask);

	public void batchSave(List<ConfTask> list);

	public void update(ConfTask confTask);

	public void delete(Integer id);

	public void batchDelete(String idseq) ;

	public List<ConfTask> pageQuery(
				Page page,
				Integer taskId, 
				Double addScoreStart, Double addScoreEnd, 
				Double subScoreStart, Double subScoreEnd, 
				Character isDuplicate, 
				Date updateTimeStart, Date updateTimeEnd, 
				String operator, 
				Integer status, 
				String orderBy, Boolean asc
			) ;

	boolean exist(Integer taskId);

}
