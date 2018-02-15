package com.qianwang.service.confsign.impl;

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

import com.qianwang.credit.dao.ConfSignDAO;
import com.qianwang.credit.entity.ConfSign;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.confsign.ConfSignService;

import javax.annotation.Resource;

/**
 * 
 * @author wangjg
 *
 */
@Service
@Transactional
public class ConfSignServiceImpl implements ConfSignService{

	@Autowired
	private ConfSignDAO confSignDAO;
	@Resource
	private RedisUtil redisUtil;

	@Override
	public ConfSign getConfSign(Integer id ) {
		return this.confSignDAO.findById(id);
	}

	@Override
	public void save(ConfSign confSign){
		delRedis();
		Date now = new Date();
		if(confSign.getId()==null){
			confSign.setCreateTime(now);
			confSign.setUpdateTime(now);
			this.confSignDAO.save(confSign);
		}else{
			confSign.setUpdateTime(now);
			this.confSignDAO.update(confSign);
		}
	}

	@Override
	public void batchSave(List<ConfSign> list){
		delRedis();
		Date now = new Date();
		for(ConfSign confSign:list){
			if(confSign.getId()==null){
				confSign.setCreateTime(now);
				confSign.setUpdateTime(now);
				this.confSignDAO.save(confSign);
			}else{
			confSign.setUpdateTime(now);
			this.confSignDAO.update(confSign);
			}
		}
	}

	@Override
	public void update(ConfSign confSign){
		delRedis();
		Date now = new Date();
		confSign.setUpdateTime(now);
		this.confSignDAO.update(confSign);
	}

	@Override
	public void delete(Integer id){
		delRedis();
		this.confSignDAO.delete(id);

	}
	
	@Override
	public void batchDelete(String idseq) {
		String[] sa = idseq.split(",");
		for(String s:sa){
			Integer id = Integer.parseInt(s);
			this.confSignDAO.delete(id);

		}
	}
	
	@Override
	public List<ConfSign> pageQuery(Page page) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("page", page);
		params.put("orderBy", "create_time");
		params.put("asc", false);
		return this.confSignDAO.queryList(params);
	}

	@Override
	public ConfSign getCurrentSign() {
		return this.confSignDAO.getCurrentSign();
	}
	
	@Override
	public List<ConfSign> historyList(Page page) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("page", page);
		params.put("orderBy", "create_time");
		params.put("asc", false);
		params.put("status", ConfSign.STATUS_OFF);
		return this.confSignDAO.queryList(params);
	}

	@Override
	public void modify(ConfSign confSign) {
		ConfSign old = this.getCurrentSign();
		if(old!=null){
			old.setStatus(ConfSign.STATUS_OFF);
			this.update(old);
		}
		confSign.setStatus(ConfSign.STATUS_NORMAL);
		updateRedis(confSign);
		this.save(confSign);
	}
	private void  delRedis(){
		String  key = RedisConst.CREDIT_CONF_SIGN.key;
		redisUtil.del(key);
	}
	private void  updateRedis(ConfSign entity){
		String  key = RedisConst.CREDIT_CONF_SIGN.key;
		redisUtil.set(key, JSON.toJSONString(entity),RedisConst.CREDIT_CONF_SIGN.expired);
	}

}
