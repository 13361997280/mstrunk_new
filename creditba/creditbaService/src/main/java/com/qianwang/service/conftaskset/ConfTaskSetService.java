package com.qianwang.service.conftaskset;

import java.util.Date;
import java.util.List;

import com.qianwang.credit.util.page.Page;

import com.qianwang.credit.entity.ConfTaskSet;

/**
 * 
 * @author wangjg
 *
 */
public interface ConfTaskSetService {

	public ConfTaskSet getConfTaskSet(Integer id);

	public void save(ConfTaskSet confTask);

	public void batchSave(List<ConfTaskSet> list);

	public void update(ConfTaskSet confTask);

	public void delete(Integer id);

	public void batchDelete(String idseq) ;

	public List<ConfTaskSet> pageQuery(Page page,String orderBy, Boolean asc) ;

	public List<ConfTaskSet> firstQuery() ;

}
