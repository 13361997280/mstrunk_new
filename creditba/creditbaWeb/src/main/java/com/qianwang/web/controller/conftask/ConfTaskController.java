package com.qianwang.web.controller.conftask;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianwang.credit.entity.ConfTask;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.conftask.ConfTaskService;
import com.qianwang.util.lang.BeanHelper;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;

/**
 * 任务配置接口
 * @author wangjg
 */
@Controller
@RequestMapping("/bus/task")
public class ConfTaskController extends BaseController{
	@Autowired
	private ConfTaskService confTaskService;

	/**
	 * 任务配置列表
	 * @param taskId 任务id
	 * @param addScoreStart 加分区间开始 
	 * @param addScoreEnd 加分区间结束
	 * @param subScoreStart 惩罚区间开始
	 * @param subScoreEnd 惩罚区间结束
	 * @param isDuplicate 是否重复
	 * @param updateTimeStart 修改时间开始
	 * @param updateTimeEnd 修改时间结束
	 * @param operator 操作账号
	 * @param status 状态
	 * @param orderBy 
	 * @param orderType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public AjaxResult list(Integer taskId, 
			Double addScoreStart, Double addScoreEnd, 
			Double subScoreStart, Double subScoreEnd, 
			Character isDuplicate, 
			Date updateTimeStart, Date updateTimeEnd, 
			String operator, 
			Integer status, String orderBy, String orderType){
		Page page = this.getPage(10);
		List<ConfTask> list = confTaskService.pageQuery(page, taskId, addScoreStart, addScoreEnd, subScoreStart, subScoreEnd, isDuplicate, updateTimeStart, updateTimeEnd, operator, status, orderBy, "asc".equals(orderType));
		return this.getPageResult(page, list);
	}

	/**
	 * 获取任务配置
	 * @param id 编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get")
	public AjaxResult get(Integer id){
		ConfTask confTask = confTaskService.getConfTask(id);
		this.checkOwner(confTask);
		return this.getSuccessResult(confTask);
	}

	/**
	 * 保存任务配置
	 */
	@ResponseBody
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public AjaxResult save(){
		ConfTask confTask = new ConfTask();
		this.bindParams(confTask);
		
		if(confTask.getId()==null){
			boolean exist = this.confTaskService.exist(confTask.getTaskId());
			if(exist){
				return this.getFailureResult("taskId已经存在！");
			}
			this.setOwner(confTask);
			confTask.setStatus(ConfTask.STATUS_NORMAL);
			this.confTaskService.save(confTask);
		}else{
			ConfTask confTask1 = this.confTaskService.getConfTask(confTask.getId());
			this.checkOwner(confTask1);
			this.setOwner(confTask);
			BeanHelper.mergeProperties(confTask, confTask1);
			this.confTaskService.update(confTask1);
		}
		return this.getSuccessResult();
	}

	/**
	 * 上架/下架
	 * @param id 编号
	 * @param status 状态 1 正常 0下架
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/enable", method=RequestMethod.POST)
	public AjaxResult enable(){
		int id = this.getIntParam("id");
		int status = this.getIntParam("status");
		ConfTask confTask = confTaskService.getConfTask(id);
		this.checkOwner(confTask);
		confTask.setStatus(status);
		this.confTaskService.update(confTask);
		return this.getSuccessResult(status);
	}

	/**
	 * 删除任务配置
	 * @param id 编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxResult delete(Integer id){
		ConfTask confTask = confTaskService.getConfTask(id);
		this.checkOwner(confTask);
		this.confTaskService.delete(id);
		return this.getSuccessResult();
	}

	/**
	 * 批量删除任务配置
	 * @param idseq 编号序列(以‘,’号隔开)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchDelete")
	public AjaxResult batchDelete(String idseq){
		this.confTaskService.batchDelete(idseq);
		return this.getSuccessResult();
	}

}
