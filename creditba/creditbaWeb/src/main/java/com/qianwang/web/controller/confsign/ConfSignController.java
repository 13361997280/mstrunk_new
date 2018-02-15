package com.qianwang.web.controller.confsign;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianwang.credit.entity.ConfSign;
import com.qianwang.credit.util.page.Page;
import com.qianwang.service.confsign.ConfSignService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;

/**
 * 签到配置接口
 * @author wangjg
 */
@Controller
@RequestMapping("/bus/sign")
public class ConfSignController extends BaseController{
	@Autowired
	private ConfSignService confSignService;

	/**
	 * 历史签到配置列表
	 * @param keyword 查询关键字
	 */
	@ResponseBody
	@RequestMapping("/historyList")
	public AjaxResult historyList(){
		Page page = this.getPage(10);
		List<ConfSign> list = confSignService.historyList(page);
		return this.getPageResult(page, list);
	}

	/**
	 * 当前签到配置
	 * @param id 编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/currentSign")
	public AjaxResult currentSign(){
		ConfSign confSign = confSignService.getCurrentSign();
		return this.getSuccessResult(confSign);
	}
	
	/**
	 * 获取签到配置
	 * @param id 编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get")
	public AjaxResult get(Integer id){
		ConfSign confSign = confSignService.getConfSign(id);
		this.checkOwner(confSign);
		return this.getSuccessResult(confSign);
	}

	/**
	 * 修改签到配置
	 */
	@ResponseBody
	@RequestMapping("/modify")
	public AjaxResult modify(){
		ConfSign confSign = new ConfSign();
		this.bindParams(confSign);
		this.setOwner(confSign);
		this.confSignService.modify(confSign);
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
		ConfSign confSign = confSignService.getConfSign(id);
		this.checkOwner(confSign);
		confSign.setStatus(status);
		this.confSignService.update(confSign);
		return this.getSuccessResult(status);
	}

}
