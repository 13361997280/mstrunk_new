package com.qianwang.web.controller.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.qianwang.dao.domain.user.UserView;
import com.qianwang.service.user.UserService;
import com.qianwang.util.security.DcPasswordEncoder;
import com.qianwang.web.controller.BaseController;
import com.qianwang.web.result.AjaxResult;
import com.qianwang.web.util.SortUtil;

@Controller
@RequestMapping("/user/**")
public class UserController extends BaseController{
	private static final Log log = LogFactory.getLog(UserController.class);
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/getAllUserTaskActivity")
	@ResponseBody
	public AjaxResult getAllUserTaskActivity(String startDate, String endDate, String startAssets, String endAssets,String type){
		return AjaxResult.successResult(userService.getAllUserTaskActivity(startDate, endDate, startAssets, endAssets,type));
	} 
	
	@RequestMapping(value = "/getUserTaskActivityTblInfo")
	@ResponseBody
	public AjaxResult getUserTaskActivityTblInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getUserTaskActivityTblInfo(startDate, endDate));
	} 
	
	@RequestMapping(value = "/getUserTaskActivityTblInfoCnt")
	@ResponseBody
	public AjaxResult getUserTaskActivityTblInfoCnt(String startDate, String endDate){
		return AjaxResult.successResult(userService.getUserTaskActivityTblInfo(startDate, endDate).size());
	} 
	
	public List<Map<String, Object>> exportUserTaskActivityTblInfo(HttpServletRequest request) {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		List<Map<String, Object>> data = userService.getUserTaskActivityTblInfo(startDate, endDate);
		return data;
	}
	
	@RequestMapping(value = "/getUserSource")
	@ResponseBody
	public AjaxResult getUserSource(String startDate,
			String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian){
		return AjaxResult.successResult(userService.getUserSource(startDate, endDate, registerChannel, spreadType, registerArea, tuijian));
	} 
	
	@RequestMapping(value = "/getUserSourceCnt")
	@ResponseBody
	public AjaxResult getUserSourceCnt(String startDate,
			String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian){
		return AjaxResult.successResult(userService.getUserSource(startDate, endDate, registerChannel, spreadType, registerArea, tuijian).size());
	} 
	@RequestMapping(value = "/getUserSourceHour")
	@ResponseBody
	public AjaxResult getUserSourceHour(String startDate,
			String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian){
		return AjaxResult.successResult(userService.getUserSourceHour(startDate, endDate, registerChannel, spreadType, registerArea, tuijian));
	} 
	
	@RequestMapping(value = "/getUserSourceHourCnt")
	@ResponseBody
	public AjaxResult getUserSourceHourCnt(String startDate,
			String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian){
		return AjaxResult.successResult(userService.getUserSourceHour(startDate, endDate, registerChannel, spreadType, registerArea, tuijian).size());
	} 
	
	@RequestMapping(value = "/getUserSourceGroup")
	@ResponseBody
	public AjaxResult getUserSourceGroup(String startDate,
			String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian){
		return AjaxResult.successResult(userService.getUserSourceGroup(startDate, endDate, registerChannel, spreadType, registerArea, tuijian));
	} 
	@RequestMapping(value = "/getUserSourceHourGroup")
	@ResponseBody
	public AjaxResult getUserSourceHourGroup(String startDate,
			String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian){
		return AjaxResult.successResult(userService.getUserSourceHourGroup(startDate, endDate, registerChannel, spreadType, registerArea, tuijian));
	} 
	
	@RequestMapping(value = "/getUserRechargeDist")
	@ResponseBody
	public AjaxResult getUserRechargeDist(String startDate, String endDate){
		return AjaxResult.successResult(userService.getUserRechargeDist(startDate, endDate));
	} 
	
	@RequestMapping(value = "/getUserSourceRec")
	@ResponseBody
	public AjaxResult getUserSourceRec(String startDate, String endDate){
		return AjaxResult.successResult(userService.getUserSourceRec(startDate, endDate));
	} 
	
	@RequestMapping(value = "/getUserSourceRecHour")
	@ResponseBody
	public AjaxResult getUserSourceRecHour(String startDate, String endDate){
		return AjaxResult.successResult(userService.getUserSourceRecHour(startDate, endDate));
	} 
	
	@RequestMapping(value = "/getPUser")
	@ResponseBody
	public AjaxResult getPUser(String presenterId, String presenterName, Integer start, Integer length, @RequestParam Map<String,String> params){
		Integer _presenterId = -1;
		try {
			if(StringUtils.isNotBlank(presenterId)){
				_presenterId = Integer.valueOf(presenterId);
			}
		} catch (Exception e) {
			log.error("error occured in parser int.");
		}
		String sort = SortUtil.getSortStr(params);
		if(StringUtils.isNotBlank(presenterName)){
			Map<String,Object> presenterMap = userService.getUserIdByName(presenterName);
			if(presenterMap!=null){
				_presenterId = (Integer) presenterMap.get("id");
			}else{
				return AjaxResult.successResult("");
			}
		}
		int cnt = presenterId==null?userService.getPUserCnt(-1):userService.getPUserCnt(_presenterId);
		return AjaxResult.successResult(cnt, cnt, userService.getPUser(_presenterId==null?-1:_presenterId, sort, start == null ? 0 : start, length == null ? 10 : length));
	} 
	@RequestMapping(value = "/getPUserCnt")
	@ResponseBody
	public AjaxResult getPUserCnt(){
		return AjaxResult.successResult(userService.getPUserCnt(-1));
	} 
	@RequestMapping(value = "/getCUser")
	@ResponseBody
	public AjaxResult getCUser(Integer presenterId, Integer start, Integer length, @RequestParam Map<String,String> params){
		String sort = SortUtil.getSortStr(params);
		int cnt = userService.getCUserCnt(presenterId);
		return AjaxResult.successResult(cnt, cnt, userService.getCUser(presenterId, sort, start == null ? 0 : start, length == null ? 10 : length));
	} 
	/**
	 * 导出指定数据
	 * @param request
	 * @return
	 */
	public List<Map<String,Object>> exportPresentInfo(HttpServletRequest request){
		Integer presenterId = -1;
		String presenterIdStr = request.getParameter("presenterId");
		String presenterName = request.getParameter("presenterName");
		List<Map<String,Object>> dataList = null;
		try {
			presenterId = Integer.valueOf(StringUtils.isBlank(presenterIdStr)?"-1":presenterIdStr);
			if(StringUtils.isNotBlank(presenterName)){
				Map<String,Object> presenterMap = userService.getUserIdByName(presenterName);
				if(presenterMap!=null){
					presenterId = (Integer) presenterMap.get("id");
				}
			}
			dataList = userService.getCUsersByPresentId(presenterId);
		} catch (Exception e) {
			log.error("error occured in exportPresentInfo.");
		}
		return dataList;
	}
	@RequestMapping(value = "/getCUserCnt")
	@ResponseBody
	public AjaxResult getCUserCnt(Integer presenterId){
		return AjaxResult.successResult(userService.getCUserCnt(presenterId));
	} 
	@RequestMapping(value = "/getTicketBee")
	@ResponseBody
	public AjaxResult getTicketBee(String startDate, String endDate, String xiaomifeng, String changdi, Integer start, Integer length, @RequestParam Map<String,String> params){
		String sort = SortUtil.getSortStr(params);
		int cnt = userService.getTicketBeeCnt(startDate, endDate, xiaomifeng, changdi);
		return AjaxResult.successResult(cnt, cnt, userService.getTicketBee(startDate, endDate, xiaomifeng, changdi, sort, start == null ? 0 : start, length == null ? 10 : length));
	} 
	public List<Map<String, Object>> getTicketBee(HttpServletRequest req){
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		String xiaomifeng = req.getParameter("xiaomifeng");
		String changdi = req.getParameter("changdi");
//		String startStr = req.getParameter("start");
//		Integer start = Integer.valueOf(startStr == null ? "0" : startStr); 
//		String lengthStr = req.getParameter("length");
//		Integer length = Integer.valueOf(lengthStr == null ? "10" : lengthStr); 
		Map<String,String> params  = req.getParameterMap();
		String sort = SortUtil.getSortStr(params);
		int cnt = userService.getTicketBeeCnt(startDate, endDate, xiaomifeng, changdi);
		return userService.getTicketBee(startDate, endDate, xiaomifeng, changdi, sort, 0, cnt);
	} 
	@RequestMapping(value = "/getTicketBeeCnt")
	@ResponseBody
	public AjaxResult getTicketBeeCnt(String startDate, String endDate, String xiaomifeng, String changdi, Integer presenterId){
		return AjaxResult.successResult(userService.getTicketBeeCnt(startDate, endDate, xiaomifeng, changdi));
	} 
	@RequestMapping(value = "/getChangdi")
	@ResponseBody
	public AjaxResult getChangdi(){
		return AjaxResult.successResult(userService.getChangdi());
	} 
	
	@RequestMapping(value = "/saveXiaomifeng")
	@ResponseBody
	public AjaxResult saveXiaomifeng(String xiaomifeng, String changdi){
		Map<String, Object> ret = new HashMap<String, Object>();		
		Map<String, Object> xmfUser = userService.getXiaomifengUserByName(xiaomifeng);
		Map<String, Object> xmf = userService.getXiaomifengByName(xiaomifeng);
		if(xmfUser == null){
			ret.put("flag", -1);
		} else if(xmf != null){
			ret = xmf;
			ret.put("flag", -2);
		} else {			
			ret.put("flag", 1);
			userService.saveXiaomifeng(xiaomifeng, changdi, getCurrentUsername());
		}
		return AjaxResult.successResult(ret);
	} 
	
	@RequestMapping(value = "/selectUserRegistRecent")
	@ResponseBody
	public AjaxResult selectUserRegistRecent(Date statDate){
		return AjaxResult.successResult(userService.selectUserRegistRecent(statDate));
	}
	
	@RequestMapping(value = "/selectUserRegist")
	@ResponseBody
	public AjaxResult  selectUserRegist(Date startDate,Date endDate){
		return AjaxResult.successResult(userService.selectUserRegist(startDate,endDate));
	}
	
	@Autowired
    @Qualifier("org.springframework.security.authenticationManager")
    protected AuthenticationManager authenticationManager;

	
	@RequestMapping(value = "/getSignInfo")
	@ResponseBody
	public AjaxResult getSignInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getSignInfo(startDate, endDate));
	} 

	@RequestMapping(value = "/getClassifySignInfo")
	@ResponseBody
	public AjaxResult getClassifySignInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getClassifySignInfo(startDate, endDate));
	}

	@RequestMapping(value = "/getSignInfoCnt")
	@ResponseBody
	public AjaxResult getSignInfoCnt(String startDate, String endDate){
		return AjaxResult.successResult(userService.getSignInfo(startDate, endDate).size());
	} 
	@RequestMapping(value = "/getSignHourInfo")
	@ResponseBody
	public AjaxResult getSignHourInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getSignHourInfo(startDate, endDate));
	} 
	@RequestMapping(value = "/getClassifySignHourInfo")
	@ResponseBody
	public AjaxResult getClassifySignHourInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getClassifySignHourInfo(startDate, endDate));
	}

	@RequestMapping(value = "/getSignHourInfoCnt")
	@ResponseBody
	public AjaxResult getSignHourInfoCnt(String startDate, String endDate){
		return AjaxResult.successResult(userService.getSignHourInfo(startDate, endDate).size());
	}
	
	@RequestMapping(value = "/getMarketIndexInfo")
	@ResponseBody
	public AjaxResult getMarketIndexInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getMarketIndexInfo(startDate, endDate));
	} 
	@RequestMapping(value = "/getMarketIndexRealInfo")
	@ResponseBody
	public AjaxResult getMarketIndexRealInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getMarketIndexRealInfo(startDate, endDate));
	} 
	@RequestMapping(value = "/getAssetsInfo")
	@ResponseBody
	public AjaxResult getAssetsInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getAssetsInfo(startDate, endDate));
	} 
	@RequestMapping(value = "/getAssetsRealInfo")
	@ResponseBody
	public AjaxResult getAssetsRealInfo(String startDate, String endDate){
		return AjaxResult.successResult(userService.getAssetsRealInfo(startDate, endDate));
	}


	@RequestMapping(value = "/findLastRegistUserRecent")
	@ResponseBody
	public AjaxResult findLastRegistUserRecent(){
		return AjaxResult.successResult(userService.findLastRegistUserRecent());
	}
}
