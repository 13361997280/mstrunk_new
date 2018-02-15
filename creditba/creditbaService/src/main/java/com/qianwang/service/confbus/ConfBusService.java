package com.qianwang.service.confbus;

import java.util.List;

import com.qianwang.credit.entity.ConfBus;
import com.qianwang.credit.util.page.Page;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfBusService {

	public ConfBus getConfBus(Integer id);

	public void save(ConfBus confBus);

	public void batchSave(List<ConfBus> list);

	public void update(ConfBus confBus);

	public void delete(Integer id);

	public void batchDelete(String idseq) ;
	
	public List<ConfBus> pageQuery(Page page, String orderBy, Boolean asc);
	
}
