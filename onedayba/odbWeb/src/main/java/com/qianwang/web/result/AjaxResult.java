package com.qianwang.web.result;

public class AjaxResult {

	/**
	 * 返回结果成功标识
	 */
	private boolean success;
	/**
	 * 数据总数
	 */
	private long totalCount;
	
	private long recordsTotal;
	
	private long recordsFiltered;
	/**
	 * 返回信息主体数据
	 */
	private Object data;
	
	/**
	 *构造方法
	 */
	public AjaxResult(boolean success,Object data){
		this.success = success;
		this.data = data;
	}
	
	/**
	 *构造方法
	 */
	public AjaxResult(long totalCount,boolean success,Object data){
		this.totalCount = totalCount;
		this.success = success;
		this.data = data;
	}
	
	public AjaxResult(long recordsTotal, long recordsFiltered,boolean success,Object data){
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
		this.success = success;
		this.data = data;
	}

	/**
	 * 返回成功信息
	 * @param data 信息数据
	 * @return 成功信息
	 */
	public static AjaxResult successResult(Object data){
		return new AjaxResult(true,data);
	}
	
	/**
	 * 返回成功信息
	 * @param data 信息数据
	 * @return 成功信息
	 */
	public static AjaxResult successResult(long totalCount,Object data){
		return new AjaxResult(totalCount,true,data);
	}
	
	public static AjaxResult successResult(long recordsTotal, long recordsFiltered, Object data){
		return new AjaxResult(recordsTotal, recordsFiltered,true,data);
	}
	
	/**
	 * 返回失败信息
	 * @param data 信息数据
	 * @return 失败信息
	 */
	public static AjaxResult failureResult(Object data){
		return new AjaxResult(false,data);
	}
	
	/**
	 * 返回成功信息
	 * @param data 信息数据
	 * @return 成功信息
	 */
	public static AjaxResult failureResult(long totalCount,Object data){
		return new AjaxResult(totalCount,false,data);
	}
	
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
}
