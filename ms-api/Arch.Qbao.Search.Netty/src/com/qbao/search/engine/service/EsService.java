package com.qbao.search.engine.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadConfig;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.CommonUtil;
import data.db.EsDataServiceSql;
import data.service.EsDataService;
import org.apache.commons.lang.StringUtils;
import util.MyCache;
import vo.EsDictPo;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class EsService {
	public static final ESLogger logger = Loggers.getLogger(EsService.class);

	/**
	 * 获取字典菜单
	 * @return
     */
	public JSONObject dict() {
		return LoadConfig.dictMap.get("menus");
	}

	/**
	 * 获取es缓存查询客群,通过searchid查询客群数据
	 * @param param
	 * @return
     */
	public String getUserIdsById(Map param) throws UnsupportedEncodingException {
		long timer = new Date().getTime();
		if(param.get("id")==null||"".equals(param.get("id"))) return null;
		String id = param.get("id").toString();
		JSONObject json = EsDataService.getInstance().getJsonObjectStrForConst("_id",id,LoadValues.GROUP_INDEX,LoadValues.GROUP_INDEX,"conditions");
		String conditions = json.getString("conditions");
		Map map = CommonUtil.convertMap(conditions);
		map.put("size",param.get("size"));
		map.put("page",param.get("page"));
		String userIds  = EsDataService.getInstance().getUserIds(map);
		logger.info("计算客群所花时间:"+(new Date().getTime() - timer) + " ms");
		return userIds;
	}
	/**
	 * 更新es缓存结果集中的客群数据,收藏客群
	 * @param param
	 * @return
     */
	public JSONObject updateUserId(Map param) throws UnsupportedEncodingException {
		long timer = new Date().getTime();
		if(param.get("id")==null||"".equals(param.get("id"))) return null;
		String id = param.get("id").toString();
		String userId = "";
		if(param.get("userId")!=null&&!"".equals(param.get("userId"))) {
			userId = param.get("userId").toString();
		}
		param.remove("id");
		param.remove("userId");
		String paramStr = CommonUtil.getParamStr(param);
		EsDataService.getInstance().saveConditionAndResult(paramStr,"",id,userId);
		logger.info("更新客群所花时间:"+(new Date().getTime() - timer) + " ms");
		JSONObject ruturnJson = new JSONObject();
		ruturnJson.put("Result", "success");
		logger.info("+++updateUserId++ Result=" + ruturnJson);
		return ruturnJson;
	}

	/**
	 * 获取用户标签主数据
	 * @param mapParam
	 * @return
     */
	public JSONObject calLabel(Map mapParam) {
		long timer = new Date().getTime();
		mapParam.remove("userId");
		//计算通用标签
		String sql = EsDataServiceSql.getInstance().getSql_Select();
		String countSql = EsDataServiceSql.getInstance().getSql_Count();
		String showList = (String)mapParam.get("showList");
		Integer page = convert(mapParam.get("page"),0);//页号
		Integer size = convert(mapParam.get("size"),LoadValues.LABEL_SIZE);//页大小

		if(StringUtils.isNotEmpty(showList)) {
			sql = sql + " and id in(" + showList + ")";
			countSql = countSql + " and id in(" + showList + ")";
		}
		sql += " and can_report_table = 1 ";
		countSql += " and can_report_table = 1";
		sql = sql +" limit "+page*size + ","+ size;
		mapParam.remove("showList");
		mapParam.remove("page");
		mapParam.remove("size");
		ArrayList<EsDictPo> results = (ArrayList<EsDictPo>)MyCache.getInstance().get("calLabelList."+sql);
		Integer count = (Integer)MyCache.getInstance().get("calLabelCount."+countSql);
		if(results==null) {
			results = EsDataServiceSql.getInstance().select(sql);
			count = EsDataServiceSql.getInstance().count(countSql);
			MyCache.getInstance().put("calLabelList." + sql, results);
			MyCache.getInstance().put("calLabelCount." + countSql, count);
		}
		int userTotal = 0;
		String cacheKey = CommonUtil.getParamStr(mapParam);
		Integer totalIn = (Integer)MyCache.getInstance().get("calLabelEsTotal."+cacheKey);
		if(totalIn==null) {
			userTotal = EsDataService.getInstance().multySearch(mapParam);
			MyCache.getInstance().put("calLabelEsTotal."+cacheKey,userTotal);
		}else{
			userTotal = totalIn;
		}
		//组装json返回给前端
		JSONObject json1 = new JSONObject();
		JSONArray jsonArray1 = new JSONArray();
		int total = 0;
		if(userTotal>0) {
			for (EsDictPo entity : results) {
				String code = entity.getLabelId();
				JSONArray jsonArray = entity.getOption();
				//没有option数据的不计算
				if (jsonArray == null || jsonArray.size() == 0) continue;
				JSONArray reportArray = new JSONArray();
				JSONObject report = new JSONObject();
				report.put("name", entity.getLabelName());
				report.put("type", entity.getGrafType());
				Map<String, String> cloneMap = CommonUtil.deepCloneMap(mapParam);
				if (mapParam.get(code) != null) {
					//条件中有字典数据
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String key = jsonObject.getString("value");
						if ("".equals(key)) continue;
						String value = jsonObject.getString("name");
						if (key.equals(mapParam.get(code))) {
							String cacheKey1 = CommonUtil.getParamStr(cloneMap);
							Integer totalIn1 = (Integer)MyCache.getInstance().get("calLabelEsCount."+cacheKey1);
							if(totalIn1==null) {
								total = EsDataService.getInstance().multySearch(cloneMap);
								MyCache.getInstance().put("calLabelEsCount."+cacheKey1,total);
							}else{
								total = totalIn1;
							}
							JSONObject jsData = new JSONObject();
							jsData.put("name", value);
							jsData.put("count", total);
							reportArray.add(jsData);
							break;
						}
					}
				} else {
					//条件中无字典数据
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String key = jsonObject.getString("value");
						if ("".equals(key)) continue;
						String value = jsonObject.getString("name");
						if (key.equals("")) {
							total = userTotal;
						} else {
							cloneMap.put(code, key);
							String cacheKey2 = CommonUtil.getParamStr(cloneMap);
							Integer totalIn2 = (Integer)MyCache.getInstance().get("calLabelEsCount."+cacheKey2);
							if(totalIn2==null) {
								total = EsDataService.getInstance().multySearch(cloneMap);
								MyCache.getInstance().put("calLabelEsCount."+cacheKey2,total);
							}else{
								total = totalIn2;
							}
						}
						JSONObject jsData = new JSONObject();
						jsData.put("name", value);
						jsData.put("count", total);
						reportArray.add(jsData);
					}
				}
				report.put("data", reportArray);
				jsonArray1.add(report);
			}
		}else{
			return json1;
		}
		json1.put("reports",jsonArray1);
		json1.put("number",userTotal);
		String spendTime = (new Date().getTime() - timer) + " ms";
		json1.put("count",count);
		json1.put("spendTime",spendTime);
		json1.put("create_date",System.currentTimeMillis());
		logger.info("计算标签查询所花时间:"+spendTime);
		return json1;
	}
	private Integer convert(Object ob,Integer defaultValue){
		if(ob==null) return defaultValue;
		if(ob.toString().equalsIgnoreCase("null")||ob.toString().equals("")) return defaultValue;
		return Integer.parseInt(ob.toString());
	}
	/**
	 * 用户id搜索minor
	 * @param param
	 * @return
     */
	public JSONObject getEntityByUserId(Map param) throws UnsupportedEncodingException {
		long timer = new Date().getTime();
		if (param.get("id") == null || "".equals(param.get("id"))) return null;
		String id = param.get("id").toString();
		String constParamStr = param.get("const") == null ? "" : param.get("const").toString();
		JSONArray dictArray = LoadConfig.dictAll.get("menus");
		JSONObject result = new JSONObject();
		JSONArray mirror = new JSONArray();
		JSONObject item = new JSONObject();
		String cacheKey = constParamStr+id;
		JSONArray minorEntity = (JSONArray) MyCache.getInstance().get("getEntityByUserId." + cacheKey);
		if (minorEntity != null) {
			mirror = minorEntity;
		} else {
			JSONObject entity = EsDataService.getInstance().getJsonObjectStrForConst("user_id", id, LoadValues.LABEL_INDEX, LoadValues.LABEL_TYPE, constParamStr);
			if (entity == null) return null;
			item.put("username", id);
			Set parentSet = new HashSet();
			Set groupSet = new HashSet();
			JSONObject item1, item2, item3, lastNode;
			JSONArray itemArray = null;
			JSONArray itemArray1 = null;
			JSONArray lastNodes = null;
			try {
				for (int i = 0; i < dictArray.size(); i++) {
					String parentName = dictArray.getJSONObject(i).getString("parentName");
					String groupName = dictArray.getJSONObject(i).getString("groupName");
					String labelName = dictArray.getJSONObject(i).getString("labelName");
					String labelId = dictArray.getJSONObject(i).getString("labelId");
					if (constParamStr != null && !"".equals(constParamStr)) {
						if (!constParamStr.contains(labelId)) continue;
					}
					String labelType = dictArray.getJSONObject(i).getString("labelType");
					String icon = dictArray.getJSONObject(i).getString("icon");
					Map<String, String> optionMap = LoadConfig.dictOption.get(labelId);
					String labelValue = "";
					if (optionMap != null) {
						labelValue = optionMap.get(entity.getString(labelId));
						if (null == labelValue || "".equals(labelValue)) {
							labelValue = entity.getString(labelId);
						}
						if (labelId.equals("user_type")) {
							if (labelValue.contains(",")) {
								String[] labelA = labelValue.split(",");
								labelValue = "";
								for (int l = 0; l < labelA.length; l++) {
									labelValue = labelValue + optionMap.get(labelA[l]) + ",";
								}
								labelValue = labelValue.substring(0, labelValue.length() - 1);
							}
						}
					} else {
						labelValue = entity.getString(labelId);
						if ("3".equals(labelType) || "4".equals(labelType)) {
							//手机归属地特殊处理 调用城市编码
							if ("3".equals(labelType)) {
								labelValue = LoadConfig.areas.get(labelValue);
							} else {
								labelValue = LoadConfig.citys.get(labelValue);
							}

							if ("".equals(labelValue) || null == labelValue) {
//							labelValue = entity.getString(labelId);
								labelValue = "未知";  //地理位置信息找不到默认显示未知
							}
						}
					}
					lastNode = new JSONObject();
					lastNodes = new JSONArray();

					lastNode.put("name", labelValue);

					lastNodes.add(lastNode);
					if (!parentSet.contains(parentName)) {
						parentSet.add(parentName);
						item1 = new JSONObject();
						item1.put("icon", icon);
						item1.put("name", parentName);
						itemArray = new JSONArray();
						if (!groupSet.contains(groupName)) {
							groupSet.add(groupName);
							item2 = new JSONObject();
							item2.put("name", groupName);
							itemArray1 = new JSONArray();
							item3 = new JSONObject();
							item3.put("name", labelName);
							if (!"".equals(labelValue) && labelValue != null) {
								item3.put("children", lastNodes);
							}
							itemArray1.add(item3);
							item2.put("children", itemArray1);
							itemArray.add(item2);
						} else {
							item3 = new JSONObject();
							item3.put("name", labelName);
							if (!"".equals(labelValue) && labelValue != null) {
								item3.put("children", lastNodes);
							}
							itemArray1.add(item3);
						}
						item1.put("children", itemArray);
						mirror.add(item1);
					} else {
						if (!groupSet.contains(groupName)) {
							groupSet.add(groupName);
							item2 = new JSONObject();
							item2.put("name", groupName);
							itemArray1 = new JSONArray();
							item3 = new JSONObject();
							item3.put("name", labelName);
							if (!"".equals(labelValue) && labelValue != null) {
								item3.put("children", lastNodes);
							}
							itemArray1.add(item3);
							item2.put("children", itemArray1);
							itemArray.add(item2);
						} else {
							item3 = new JSONObject();
							item3.put("name", labelName);
							if (!"".equals(labelValue) && labelValue != null) {
								item3.put("children", lastNodes);
							}
							itemArray1.add(item3);
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			MyCache.getInstance().put("getEntityByUserId."+cacheKey,mirror);
		}
		item.put("mirror",mirror);
		long time = new Date().getTime() - timer;
		result.put("spendTime",time + " ms");
		result.put("result",item);
		logger.info("计算userminor所花时间:"+time+"ms");
		return result;
	}

	/**
	 * 多条件查询列表
	 * @param param
	 * @return
     */
	public JSONObject multySearchForEntity(Map param) {
		long timer = new Date().getTime();
		String cacheKey = CommonUtil.getParamStr(param);
		JSONObject entity = (JSONObject)MyCache.getInstance().get("multySearchForEntity."+cacheKey);
		if(entity==null) {
			entity = EsDataService.getInstance().multySearchForEntity(param);
			MyCache.getInstance().put("multySearchForEntity."+cacheKey,entity);
		}
		String time = (new Date().getTime() - timer) + " ms";
		if(entity!=null){
			entity.put("spendTime",time);
		}
		logger.info("多条件查询列表所花时间:"+time);
		return entity;

	}
	/**
	 * 多条件查询列表
	 * @param param
	 * @return
     */
	public JSONObject getSearchConstForFront(Map param) throws UnsupportedEncodingException {
		if(param.get("id")==null||"".equals(param.get("id"))) return null;
		String id = param.get("id").toString();
		return EsDataService.getInstance().getSearchConstForFront("_id",id);
	}
	/**
	 * 获取字典菜单
	 * @param param
	 * @return
	 */
	public String saveLog(Map param) {
		try{
			EsDataService.getInstance().saveLog(param);
		}catch (Exception ex){
			logger.error("EsService:saveLog:",ex.getMessage());
		}
		return "success";
	}
	/**
	 * 通过key获取properties的value
	 * @param param
	 * @return
	 */
	public String getProperties(Map param) {
		String value="";
		try{
			String key = convert(param.get("key"));
			value =LoadConfig.properties.get(key);
			if(value==null){
				value = Config.getMainProperties().get(key);
			}
			if(value==null){
				value = Config.getMainPropertiesByParent().get(key);
			}
		}catch (Exception ex){
			logger.error("EsService:getProperties:",ex.getMessage());
		}
		return value;
	}
	public Integer getActiveUserCount(){
		Integer count = EsDataService.getInstance().multySearch(null);
		logger.info("EsService.getActiveUserCount is"+count);
		return count;
	}

	/**
	 * 获取日志
	 * @return
	 */
	public JSONObject getLog(Map param){
		return EsDataService.getInstance().getLog(param);
	}

	private String convert(Object object){
		if(object==null) return null;
		return object.toString();
	}
	public static void main(String[] arg){
		//EsDataService.getInstance().getJsonObjectStrForConst("condition","base_sex=0,1",LoadValues.RESULT_INDEX,LoadValues.RESULT_TYPE);
	}

}
