package com.qianwang.credit.util.page;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 
 * @author wangjg
 *
 */
class PageWrapper extends Page{

	private Object pageObject;
	private PageParam pageParam;

	public PageWrapper(Object pageParam, PageParam pageAnno) {
		this.pageObject = pageParam;
		this.pageParam = pageAnno;
		this.set();
	}
	
	private void set(){
		this.setPageSize((Integer) getProperty(pageObject, pageParam.pageSize()));
		this.setPageNo((Integer) getProperty(pageObject, pageParam.pageNo()));
		this.setRowTotal((Long) getProperty(pageObject, pageParam.rowTotal()));
	}
	
	public void setRowTotal(Long total) {
		this.setProperty(pageObject, pageParam.rowTotal(), total);
	}
	
	private void setProperty(Object bean, String property, Object value){
		try{
			PropertyUtils.setProperty(bean, property, value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Object getProperty(Object bean, String property){
		try{
			return PropertyUtils.getProperty(bean, property);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
