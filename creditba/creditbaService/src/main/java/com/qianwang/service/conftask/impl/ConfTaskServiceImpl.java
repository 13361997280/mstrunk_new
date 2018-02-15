package com.qianwang.service.conftask.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.qianwang.constant.RedisConst;
import com.qianwang.util.Redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianwang.credit.dao.ConfTaskDAO;
import com.qianwang.credit.entity.ConfTask;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.conftask.ConfTaskService;

import javax.annotation.Resource;

/**
 * 
 * @author wangjg
 *
 */
@Service
@Transactional
public class ConfTaskServiceImpl implements ConfTaskService{

	@Autowired
	private ConfTaskDAO confTaskDAO;
	@Resource
	private RedisUtil redisUtil;

	@Override
	public ConfTask getConfTask(Integer id ) {
		return this.confTaskDAO.findById(id);
	}

	@Override
	public void save(ConfTask confTask){
		Date now = new Date();
		if(confTask.getId()==null){
			confTask.setCreateTime(now);
			confTask.setUpdateTime(now);
			updateRedis(confTask);
			this.confTaskDAO.save(confTask);
		}else{
			confTask.setUpdateTime(now);
			updateRedis(confTask);
			this.confTaskDAO.update(confTask);
		}
	}
	
	@Override
	public boolean exist(Integer taskId){
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		Long count = this.confTaskDAO.queryCount(params);
		return (count>0);
	}

	@Override
	public void batchSave(List<ConfTask> list){
		Date now = new Date();
		for(ConfTask confTask:list){
			if(confTask.getId()==null){
				confTask.setCreateTime(now);
				confTask.setUpdateTime(now);
				updateRedis(confTask);
				this.confTaskDAO.save(confTask);
			}else{
			confTask.setUpdateTime(now);
			updateRedis(confTask);
			this.confTaskDAO.update(confTask);
			}
		}
	}

	@Override
	public void update(ConfTask confTask){
		Date now = new Date();
		confTask.setUpdateTime(now);
		updateRedis(confTask);
		this.confTaskDAO.update(confTask);
	}

	@Override
	public void delete(Integer id){
		delRedis(getConfTask(id));
		this.confTaskDAO.delete(id);

	}

	@Override
	public void batchDelete(String idseq) {
		String[] sa = idseq.split(",");
		for(String s:sa){
			Integer id = Integer.parseInt(s);
			this.delete(id);

		}
	}

	@Override
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
		) 
	{
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("page", page);
		params.put("taskId", taskId);
		params.put("addScoreStart", addScoreStart);
		params.put("addScoreEnd", addScoreEnd);
		params.put("subScoreStart", subScoreStart);
		params.put("subScoreEnd", subScoreEnd);
		params.put("isDuplicate", isDuplicate);
		params.put("updateTimeStart", updateTimeStart);
		params.put("updateTimeEnd", updateTimeEnd);
		params.put("operator", operator);
		params.put("status", status);
		params.put("orderBy", orderBy);
		params.put("asc", asc);
		return this.confTaskDAO.queryList(params);
	}

	private void  delRedis(ConfTask entity){
		String  key = String.format(RedisConst.CREDIT_CONF_TASK.key,entity.getTaskId());
		redisUtil.del(key);
	}
	private void  updateRedis(ConfTask entity){
		String  key = String.format(RedisConst.CREDIT_CONF_TASK.key,entity.getTaskId());
		redisUtil.set(key, JSON.toJSONString(entity),RedisConst.CREDIT_CONF_TASK.expired);
	}
}
