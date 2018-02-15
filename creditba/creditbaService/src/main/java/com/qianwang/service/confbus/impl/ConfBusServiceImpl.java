package com.qianwang.service.confbus.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qianwang.constant.RedisConst;
import com.qianwang.util.Redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianwang.credit.dao.ConfBusDAO;
import com.qianwang.credit.entity.ConfBus;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.confbus.ConfBusService;

import javax.annotation.Resource;

/**
 * 
 * @author wangjg
 *
 */
@Service
@Transactional
public class ConfBusServiceImpl implements ConfBusService{

	@Autowired
	private ConfBusDAO confBusDAO;
	@Resource
	private RedisUtil redisUtil;

	@Override
	public ConfBus getConfBus(Integer id ) {
		return this.confBusDAO.findById(id);
	}

	@Override
	public void save(ConfBus confBus){
		delRedis();
		Date now = new Date();
		if(confBus.getBusId()==null){
			confBus.setCreateTime(now);
			confBus.setUpdateTime(now);
			this.confBusDAO.save(confBus);
		}else{
			confBus.setUpdateTime(now);
			this.confBusDAO.update(confBus);
		}
	}

	@Override
	public void batchSave(List<ConfBus> list){
		delRedis();
		Date now = new Date();
		for(ConfBus confBus:list){
			if(confBus.getBusId()==null){
				confBus.setCreateTime(now);
				confBus.setUpdateTime(now);
				this.confBusDAO.save(confBus);
			}else{
			confBus.setUpdateTime(now);
			this.confBusDAO.update(confBus);
			}
		}
	}

	@Override
	public void update(ConfBus confBus){
		delRedis();
		Date now = new Date();
		confBus.setUpdateTime(now);
		this.confBusDAO.update(confBus);
	}

	@Override
	public void delete(Integer id){
		delRedis();
		this.confBusDAO.delete(id);

	}

	@Override
	public void batchDelete(String idseq) {
		delRedis();
		String[] sa = idseq.split(",");
		for(String s:sa){
			Integer id = Integer.parseInt(s);
			this.confBusDAO.delete(id);

		}
	}
	
	@Override
	public List<ConfBus> pageQuery(Page page, String orderBy, Boolean asc) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("page", page);
		params.put("orderBy", orderBy);
		params.put("asc", asc);
		return this.confBusDAO.queryList(params);
	}
	private void  delRedis(){
		String  key = String.format(RedisConst.CREDIT_CONF_BUS.key);
		redisUtil.del(key);
	}
}
