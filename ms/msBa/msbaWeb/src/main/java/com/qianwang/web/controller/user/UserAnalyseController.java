package com.qianwang.web.controller.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianwang.dao.domain.user.UserAnalyseStat;
import com.qianwang.service.user.UserAnalyseService;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import com.qianwang.web.util.SortUtil;
@Controller
@RequestMapping("/userAnalyse/**")
public class UserAnalyseController extends BaseController {

	@Autowired
	private UserAnalyseService userAnalyseService;
	/**
	 * 查询注册用户属性分析数据
	 * @param startDate
	 * @param endDate
	 * @param downCookieNum
	 * @param upCookieNum
	 * @param downIpNum
	 * @param upIpNum
	 * @param downFpNum
	 * @param upFpNum
	 * @param start
	 * @param length
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getRegisterUserAnalyseSstat")
	@ResponseBody
	public AjaxResult getRegisterUserAnalyseSstat(
		Date startDate,
		Date endDate,
		
		Integer downCookieNum, 
		Integer upCookieNum, 
		Integer downIpNum, 
		Integer upIpNum,
		Integer downFpNum,
		Integer upFpNum,
		Integer downDevIdNum,
		Integer upDevIdNum,
		Integer downSequnetCardNum,
		Integer upSequnetCardNum,
		Integer downCardNum,
		Integer upCardNum,
		Integer downPresentNum,
		Integer upPresentNum,
		
		Integer channelId,
		Integer spreadTypeId,
		Integer districtId,
		//推荐人ID
		Integer presentUserId,
		//分页
		Integer start, Integer length, @RequestParam Map<String,String> params
		){
		String sort = SortUtil.getSortStr(params);
		Integer count =  userAnalyseService.countUserAnalyseStat(startDate, endDate, presentUserId,
				downCookieNum, upCookieNum, 
				downIpNum, upIpNum, 
				downFpNum, upFpNum,
				downDevIdNum,upDevIdNum,
				downSequnetCardNum,upSequnetCardNum,
				downCardNum,upCardNum,
				downPresentNum,upPresentNum,
				
				channelId, spreadTypeId, districtId);
		List<UserAnalyseStat> dataList = userAnalyseService.selectUserAnalyseStat(startDate, endDate, presentUserId, 
				downCookieNum, upCookieNum, 
				downIpNum, upIpNum, 
				downFpNum, upFpNum,
				downDevIdNum,upDevIdNum,
				downSequnetCardNum,upSequnetCardNum,
				downCardNum,upCardNum,
				downPresentNum,upPresentNum,
				
				channelId, spreadTypeId, districtId,
				start, length, sort);
		return AjaxResult.successResult(count,count, dataList);
	}
}
