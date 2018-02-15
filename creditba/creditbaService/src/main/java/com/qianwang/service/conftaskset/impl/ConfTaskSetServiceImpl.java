package com.qianwang.service.conftaskset.impl;

import com.alibaba.fastjson.JSON;
import com.qianwang.constant.RedisConst;
import com.qianwang.credit.dao.ConfTaskSetDAO;
import com.qianwang.credit.entity.ConfTaskSet;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.conftaskset.ConfTaskSetService;
import com.qianwang.util.Redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author wangjg
 *
 */
@Service
@Transactional
public class ConfTaskSetServiceImpl implements ConfTaskSetService {

	@Autowired
	private ConfTaskSetDAO ConfTaskSetDAO;
	@Resource
	private RedisUtil redisUtil;

	@Override
	public ConfTaskSet getConfTaskSet(Integer id ) {
		return this.ConfTaskSetDAO.findById(id);
	}

	@Override
	public void save(ConfTaskSet ConfTaskSet){
		delRedis();
		Date now = new Date();
		if(ConfTaskSet.getId()==null){
			ConfTaskSet.setCreateTime(now);
			ConfTaskSet.setUpdateTime(now);
			this.ConfTaskSetDAO.save(ConfTaskSet);
		}else{
			ConfTaskSet.setUpdateTime(now);
			this.ConfTaskSetDAO.update(ConfTaskSet);
		}
	}

	@Override
	public void batchSave(List<ConfTaskSet> list){
		delRedis();
		Date now = new Date();
		for(ConfTaskSet ConfTaskSet:list){
			if(ConfTaskSet.getId()==null){
				ConfTaskSet.setCreateTime(now);
				ConfTaskSet.setUpdateTime(now);
				this.ConfTaskSetDAO.save(ConfTaskSet);
			}else{
			ConfTaskSet.setUpdateTime(now);
			this.ConfTaskSetDAO.update(ConfTaskSet);
			}
		}
	}

	@Override
	public void update(ConfTaskSet ConfTaskSet){
		delRedis();
		Date now = new Date();
		ConfTaskSet.setUpdateTime(now);
		this.ConfTaskSetDAO.update(ConfTaskSet);
	}

	@Override
	public void delete(Integer id){
		delRedis();
		this.ConfTaskSetDAO.delete(id);

	}

	@Override
	public void batchDelete(String idseq) {
		delRedis();
		String[] sa = idseq.split(",");
		for(String s:sa){
			Integer id = Integer.parseInt(s);
			this.ConfTaskSetDAO.delete(id);

		}
	}

	@Override
	public List<ConfTaskSet> pageQuery(Page page,String orderBy, Boolean asc)
	{
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("page", page);
		params.put("orderBy", orderBy);
		params.put("asc", asc);
		return this.ConfTaskSetDAO.queryList(params);
	}

	@Override
	public List<ConfTaskSet> firstQuery() {
		return this.ConfTaskSetDAO.firstQuery();
	}

	private void  delRedis(){
		String  key = RedisConst.CREDIT_CONF_TASK_SET.key;
		redisUtil.del(key);
	}
}
