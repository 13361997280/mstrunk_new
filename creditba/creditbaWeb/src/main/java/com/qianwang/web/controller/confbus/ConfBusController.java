package com.qianwang.web.controller.confbus;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianwang.credit.entity.ConfBus;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.confbus.ConfBusService;
import com.qianwang.util.lang.BeanHelper;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;

/**
 * 业务信用分配置表接口
 * @author wangjg
 */
@Controller
@RequestMapping("/bus")
public class ConfBusController extends BaseController{
	@Autowired
	private ConfBusService confBusService;

	/**
	 * 业务信用分配置表列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public AjaxResult list(String orderBy, String orderType){
		Page page = this.getPage(10);
		List<ConfBus> list = confBusService.pageQuery(page, orderBy, "asc".equals(orderType));
		return this.getPageResult(page, list);
	}

	/**
	 * 获取业务信用分配置表
	 * @param id 编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get")
	public AjaxResult get(Integer id){
		ConfBus confBus = confBusService.getConfBus(id);
		return this.getSuccessResult(confBus);
	}

	/**
	 * 保存业务信用分配置表
	 */
	@ResponseBody
	@RequestMapping("/save")
	public AjaxResult save(){
		ConfBus confBus = new ConfBus();
		this.bindParams(confBus);
		
		if(confBus.getBusId()==null){
			confBus.setStatus(ConfBus.STATUS_NORMAL);
			this.confBusService.save(confBus);
		}else{
			ConfBus confBus1 = this.confBusService.getConfBus(confBus.getBusId());
			BeanHelper.mergeProperties(confBus, confBus1);
			this.confBusService.update(confBus1);
		}
		return this.getSuccessResult();
	}

	/**
	 * 删除业务信用分配置表
	 * @param id 编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxResult delete(Integer id){
		this.confBusService.delete(id);
		return this.getSuccessResult();
	}

	/**
	 * 批量删除业务信用分配置表
	 * @param idseq 编号序列(以‘,’号隔开)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/batchDelete")
	public AjaxResult batchDelete(String idseq){
		this.confBusService.batchDelete(idseq);
		return this.getSuccessResult();
	}

}
