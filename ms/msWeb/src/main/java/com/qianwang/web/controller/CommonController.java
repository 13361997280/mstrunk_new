package com.qianwang.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qianwang.service.properties.UrlProperties;
import com.qianwang.service.redis.RedisConst;
import com.qianwang.service.redis.RedisUtil;
import com.qianwang.service.user.CityService;
import com.qianwang.service.user.UserService;
import com.qianwang.util.http.HttpUtils;
import com.qianwang.web.result.SimpleResult;
import com.qianwang.web.vo.IndexContentDetail;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/index")
public class CommonController  extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private CityService cityService;
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	UrlProperties urlProperties;

	Map cityMap = new HashMap(3);
	@RequestMapping(value = "/userNumber")
	@ResponseBody
	public SimpleResult userNumber(HttpServletRequest request, HttpServletResponse response)throws Exception{

		Map map = new HashMap();
		String userCount=null;
		String activityInfo=null;
        try {
			//一期暂时在代码里写死
			List list  = new ArrayList();
	/*		list.add(new IndexContentDetail("人口标签","people","人口标签人口标签人口标签<br />人口标签人口标签人口标签"));
			list.add(new IndexContentDetail("购物标签","bag","购物标签购物标签购物标签购物<br />标签购物标签购物标签购物标签"));
			list.add(new IndexContentDetail("上网标签","arrow","上网标签上网标签上网标签上<br />网标签上网标签上网标签上网标签"));
			list.add(new IndexContentDetail("投资标签","pig","投资标签投资标签投资标签投资<br />标签投资标签投资标签投资标签"));
			list.add(new IndexContentDetail("娱乐标签","music","娱乐标签娱乐标签娱乐标签娱<br />乐标签娱乐标签娱乐标签娱乐标签娱乐标签"));
			list.add(new IndexContentDetail("人脉标签","group","人脉标签人脉标签人脉标签人<br />脉标签人脉标签人脉标签人脉标签人脉标签"));
			list.add(new IndexContentDetail("位置标签","place","位置标签位置标签位置标签位<br />置标签位置标签位置标签位置标签位置标签"));
*/
			String info= redisUtil.get(RedisConst.MS_INDEX_ACTIVITY_INFO.key);
			if(StringUtils.isEmpty(info)){
				activityInfo = HttpUtils.get(urlProperties.getIndexActivityInfo);
				redisUtil.set(RedisConst.MS_INDEX_ACTIVITY_INFO.key,activityInfo,RedisConst.MS_INDEX_ACTIVITY_INFO.expired);
			}else{
				activityInfo=info;
			}
			JSONArray jsonArr = (JSONArray) JSON.parse(activityInfo);
			Object propertyName=null,propertyValue=null,remark=null;
			for(int i=0;i<jsonArr.size();i++) {
				JSONObject j =jsonArr.getJSONObject(i);
				propertyName=j.get("property_name");
				propertyValue=j.get("property_value");
				remark=j.get("remark");
				list.add(new IndexContentDetail(propertyValue!=null?propertyValue.toString():"",propertyName!=null?propertyName.toString().substring(propertyName.toString().lastIndexOf(".")+1):"",remark!=null?remark.toString():""));
            }
			String count=redisUtil.get(RedisConst.MS_ACTIVITY_USER_COUNT.key);
			if(StringUtils.isEmpty(count)){
				userCount= HttpUtils.get(urlProperties.getActiveUserCount);
				redisUtil.set(RedisConst.MS_ACTIVITY_USER_COUNT.key,userCount,RedisUtil.calTime(Integer.parseInt
						(urlProperties.redisExpiredTime)));
				log.info("CommonController.userNumber from es");
			}else {
				log.info("CommonController.userNumber from redis");
				userCount=count;
			}
			map.put("number",userCount);
			map.put("list",list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SimpleResult.successResult("首页数据",map);
	}

	@RequestMapping(value = "/cityCode")
	@ResponseBody
	public SimpleResult cityCode(HttpServletRequest request, HttpServletResponse response)throws Exception{
		try {
			String provinceString=redisUtil.get(RedisConst.MS_PROVINCE_CODE.key);
			if(StringUtils.isEmpty(provinceString) || provinceString.equals("[]")){
				provinceString=JSON.toJSONString(cityService.getAllProvince());
				redisUtil.set(RedisConst.MS_PROVINCE_CODE.key,provinceString,RedisConst.MS_PROVINCE_CODE.expired);
			}
			String cityString=redisUtil.get(RedisConst.MS_CITY_CODE.key);
			if(StringUtils.isEmpty(cityString) || cityString.equals("[]")){
				cityString=JSON.toJSONString(cityService.getAllCity());
				redisUtil.set(RedisConst.MS_CITY_CODE.key,cityString,RedisConst.MS_CITY_CODE.expired);
			}
			String areaString=redisUtil.get(RedisConst.MS_AREA_CODE.key);
			if(StringUtils.isEmpty(areaString) || areaString.equals("[]")){
				areaString=JSON.toJSONString(cityService.getAllArea());
				redisUtil.set(RedisConst.MS_AREA_CODE.key,areaString,RedisConst.MS_AREA_CODE.expired);
			}

			cityMap.put("province",JSON.parseArray(provinceString));
			cityMap.put("city",JSON.parseArray(cityString));
			cityMap.put("area",JSON.parseArray(areaString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*try {
			resultMap=redisUtil.getMap("cityCode");
			if(resultMap==null || resultMap.size()==0){
				Map map = new HashMap();
				map.put("province",cityService.getAllProvince());
				map.put("city",cityService.getAllCity());
				map.put("area",cityService.getAllArea());
				redisUtil.setMap(RedisConst.MS_CITY_CODE.key,map, RedisConst.MS_CITY_CODE.expired);
				resultMap=map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return SimpleResult.successResult("省市区",cityMap);
	}
}
;