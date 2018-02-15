package data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import util.NewsCache;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaDataService {

	private static ESLogger logger = Loggers.getLogger(BaDataService.class);
	private static BaDataService service;
	private static DecimalFormat df;
	public static final BaDataService getInstance(){
		try {
			if (service == null) {
				service = new BaDataService();
				df   = new DecimalFormat("######0.00");
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return service;
	}
	/**
	 * 条件拼装
	 * @param bool
	 * @param fieldName
	 * @param fieldValue
	 */
	private void getCondition(BoolQueryBuilder bool,String fieldName,String fieldValue,boolean isDateTime){
		if (StringUtils.isNotEmpty(fieldValue)&&!",".equals(fieldValue)) {
			String[] paramStrs = fieldValue.split(",");
			if(paramStrs.length==1){
				if(isDateTime)fieldValue=fieldValue+".000";
				bool.must(QueryBuilders.rangeQuery(fieldName).gte(fieldValue));
			}else{
				String start = paramStrs[0];
				String end = paramStrs[1];
				if(StringUtils.isNotEmpty(start)&&StringUtils.isNotEmpty(end)){
					if(isDateTime){
						if(start.length()>10) {
							start = start + ".000";
							end = end + ".999";
						}else{
							start = start + " 00:00:00.000";
							end = end + " 23:59:59.999";
						}
					}
					bool.must(QueryBuilders.rangeQuery(fieldName).gte(start).lte(end));
				}else if(StringUtils.isNotEmpty(start)&&StringUtils.isEmpty(end)){
					if(isDateTime){
						if(start.length()>10) {
							start = start + ".000";
						}else{
							start = start + " 00:00:00.000";
						}
					}
					bool.must(QueryBuilders.rangeQuery(fieldName).gte(start));
				}else if(StringUtils.isEmpty(start)&&StringUtils.isNotEmpty(end)){
					if(isDateTime){
						if(end.length()>10) {
							end = end + ".999";
						}else{
							end = end + " 23:59:59.999";
						}
						end = end+".999";
					}
					bool.must(QueryBuilders.rangeQuery(fieldName).lte(end));
				}
			}
		}
	}

	/**
	 * 后台查询信用总分列表
	 * @param map
	 * @return
	 */
	public JSONObject search(Map<String,Object> map) {
		long timer = new Date().getTime();
		JSONObject returnJson = new JSONObject();
		try {
			String userId = (String)map.get("userId");
			Integer page = map.get("page")==null?1:Integer.parseInt(map.get("page").toString());
			Integer size = map.get("size")==null?10:Integer.parseInt(map.get("size").toString());
			String ratios = map.get("ratios")==null?null:map.get("ratios").toString();
			String totalScores = map.get("totalScores")==null?null:map.get("totalScores").toString();
			String creditScores = map.get("creditScores")==null?null:map.get("creditScores").toString();
			String addScores = map.get("addScores")==null?null:map.get("addScores").toString();
			String adjustScores = map.get("adjustScores")==null?null:map.get("adjustScores").toString();
			String adjustTimes = map.get("adjustTimes")==null?null:map.get("adjustTimes").toString();
			String addTimes = map.get("addTimes")==null?null:map.get("addTimes").toString();
			//缓存
			String cacheKey = "searchList."+userId+page+size+ratios+totalScores+addScores+adjustScores+adjustTimes+addTimes;
			JSONObject entity = (JSONObject) NewsCache.getInstance().get(cacheKey);
			if(entity != null){
				//return entity;
			}
			returnJson.put("page",page);
			returnJson.put("size",size);
			JSONArray newsArray = new JSONArray();
			Long total = 0l;

			//条件组装
			BoolQueryBuilder bool = QueryBuilders.boolQuery();
			if(StringUtils.isNotEmpty(userId)){
				bool.must(QueryBuilders.termQuery("user_id", userId));
			}
			getCondition(bool,"ratio",ratios,false);
			getCondition(bool,"total_score",totalScores,false);
			getCondition(bool,"add_score",addScores,false);
			getCondition(bool,"credit_score",creditScores,false);
			getCondition(bool,"adjust_score",adjustScores,false);
			getCondition(bool,"adjust_time",adjustTimes,true);
			getCondition(bool,"add_time",addTimes,true);

			String[] fields = {"user_id", "ratio", "total_score", "credit_score", "add_score", "add_time", "adjust_score", "adjust_time"};
			FetchSourceContext sourceContext = new FetchSourceContext(true, fields, null);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.fetchSource(sourceContext);
			searchSourceBuilder.postFilter(bool);

			SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
					.setIndices(LoadValues.SCORE_INDEX)
					.setTypes(LoadValues.SCORE_INDEX)
					.setSource(searchSourceBuilder)
					.setFrom((page - 1) * size)
					.setSize(size)
					.setQuery(bool).addSort("update_time", SortOrder.DESC);
			SearchResponse response = requestBuilder.execute().actionGet();
			SearchHits hits = response.getHits();
			total = hits.getTotalHits();
			for (SearchHit hit : hits.getHits()) {
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				JSONObject object1 = new JSONObject();
				object1.put("userId", object.getString("user_id"));
				object1.put("ratio", formatDoubleStr(object.getDoubleValue("ratio")));
				object1.put("totalScore", object.getString("total_score"));
				object1.put("creditScore", object.getString("credit_score"));
				object1.put("addScore", object.getString("add_score"));
				object1.put("lastAddTime", object.getString("add_time"));
				object1.put("adjustScore", object.getString("adjust_score"));
				object1.put("lastAdjustTime", object.getString("adjust_time"));
				newsArray.add(object1);
			}
			returnJson.put("items",newsArray);
			returnJson.put("total",total);
			NewsCache.getInstance().put(cacheKey,returnJson);
		}catch (Exception ex){
			returnJson.put("message",ex.toString());
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询信用总分列表所花时间:"+time);
		}
		return returnJson;
	}
	/**
	 * 后台查询信用明细列表(上半部分)
	 * @param map
	 * @return
	 */
	public JSONObject searchDetailUp(Map<String,Object> map) {
		long timer = new Date().getTime();
		Integer userId = Integer.parseInt(map.get("userId").toString());
		//缓存
		String cacheKey = "searchDetailUp."+userId;
		JSONObject entity = (JSONObject) NewsCache.getInstance().get(cacheKey);
		if(entity != null){
			//return entity;
		}

		JSONObject returnJson = new JSONObject();
		try {
			//条件组装
			BoolQueryBuilder bool = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("user_id",userId));
			String[] fields = {"user_id", "ratio", "total_score", "credit_score","sign_score", "sign_fre"};
			FetchSourceContext sourceContext = new FetchSourceContext(true, fields, null);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.fetchSource(sourceContext);
			searchSourceBuilder.postFilter(bool);

			SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
					.setIndices(LoadValues.SCORE_INDEX)
					.setTypes(LoadValues.SCORE_INDEX)
					.setSource(searchSourceBuilder)
					.setQuery(bool).addSort("update_time", SortOrder.DESC);
			SearchResponse response = requestBuilder.execute().actionGet();
			SearchHits hits = response.getHits();

			for (SearchHit hit : hits.getHits()) {
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				returnJson.put("userId", userId);
				returnJson.put("ratio", object.getString("ratio"));
				returnJson.put("totalScore", object.getString("total_score"));
				returnJson.put("creditScore", object.getString("credit_score"));
				Double signScore = object.getDoubleValue("sign_score");
				Integer signFre = object.getIntValue("sign_fre");
				returnJson.put("addScore", getScore(userId,false)+signScore);
				returnJson.put("addScoreFre", getScoreFre(userId,true,false)+signFre);
				returnJson.put("subScoreFre", getScoreFre(userId,false,false));
				returnJson.put("adjustScore", getScore(userId,true));
				returnJson.put("adjustAddScoreFre", getScoreFre(userId,true,true));
				returnJson.put("adjustSubScoreFre", getScoreFre(userId,false,true));
			}
			NewsCache.getInstance().put(cacheKey,returnJson);
		}catch (Exception ex){
			returnJson.put("message",ex.toString());
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询信用明细列表所花时间:"+time);
		}
		return returnJson;
	}
	/**
	 * 后台查询信用明细列表(下半部分)
	 * @param map
	 * @return
	 */
	public JSONObject searchDetailDown(Map<String,Object> map) {
		long timer = new Date().getTime();
		String userId = (String)map.get("userId");
		Integer page = map.get("page")==null?1:Integer.parseInt(map.get("page").toString());
		Integer size = map.get("size")==null?10:Integer.parseInt(map.get("size").toString());
		//缓存
		String cacheKey = "searchDetailDown."+userId+page+size;
		JSONObject entity = (JSONObject) NewsCache.getInstance().get(cacheKey);
		if(entity != null){
			//return entity;
		}

		JSONObject returnJson = new JSONObject();
		returnJson.put("page",page);
		returnJson.put("size",size);
		JSONArray newsArray = new JSONArray();
		Long total = 0l;
		try {
			//条件组装
			BoolQueryBuilder bool = QueryBuilders.boolQuery();
			if(StringUtils.isNotEmpty(userId)){
				bool.must(QueryBuilders.termQuery("user_id", userId));
			}
			String[] fields = {"bus_type", "bus_id", "score", "update_time", "valid_start_time", "valid_end_time", "action", "memo", "operator"};
			FetchSourceContext sourceContext = new FetchSourceContext(true, fields, null);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.fetchSource(sourceContext);
			searchSourceBuilder.postFilter(bool);

			SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
					.setIndices(LoadValues.SCORE_DETAIL_INDEX)
					.setTypes(LoadValues.SCORE_DETAIL_INDEX)
					.setSource(searchSourceBuilder)
					.setFrom((page - 1) * size)
					.setSize(size)
					.setQuery(bool).addSort("update_time", SortOrder.DESC);
			SearchResponse response = requestBuilder.execute().actionGet();
			SearchHits hits = response.getHits();
			total = hits.getTotalHits();
			for (SearchHit hit : hits.getHits()) {
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				JSONObject object1 = new JSONObject();
				object1.put("busName", object.getString("bus_type"));
				object1.put("busId", object.getString("bus_id"));
				object1.put("score", object.getString("score"));
				object1.put("adjustTime", object.getString("update_time"));
				object1.put("validStartTime", object.getString("valid_start_time"));
				object1.put("validEndTime", object.getString("valid_end_time"));
				object1.put("status", getStatus(object.getIntValue("action")));
				object1.put("memo", object.getString("memo"));
				object1.put("operator", object.getString("operator"));
				newsArray.add(object1);
			}
			returnJson.put("items",newsArray);
			returnJson.put("total",total);
			NewsCache.getInstance().put(cacheKey,returnJson);
		}catch (Exception ex){
			returnJson.put("message",ex.toString());
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询信用明细列表所花时间:"+time);
		}
		return returnJson;
	}
	/**
	 * 后台查询信用加分项明细列表
	 * @param map
	 * @return
	 */
	public JSONObject searchAddScoreDetailList(Map<String,Object> map) {
		long timer = new Date().getTime();
		String userId = (String)map.get("userId");
		Integer page = map.get("page")==null?1:Integer.parseInt(map.get("page").toString());
		Integer size = map.get("size")==null?10:Integer.parseInt(map.get("size").toString());
		String busType = map.get("busType")==null?null:map.get("busType").toString();
		String busId = map.get("busId")==null?null:map.get("busId").toString();
		String operator = map.get("operator")==null?null:map.get("operator").toString();
		String scoreFlag = map.get("scoreFlag")==null?null:map.get("scoreFlag").toString();
		Integer status = map.get("status")==null?null:Integer.parseInt(map.get("status").toString());
		String adjustTimes = map.get("adjustTimes")==null?null:map.get("adjustTimes").toString();
		String validTimes = map.get("validTimes")==null?null:map.get("validTimes").toString();
		//缓存
		String cacheKey = "searchAddScoreDetailList."+userId+page+size+busType+busId+operator+scoreFlag+adjustTimes+validTimes+status;
		JSONObject entity = (JSONObject) NewsCache.getInstance().get(cacheKey);
		if(entity != null){
			//return entity;
		}

		JSONObject returnJson = new JSONObject();
		returnJson.put("page",page);
		returnJson.put("size",size);
		JSONArray newsArray = new JSONArray();
		Long total = 0l;
		try {
			//条件组装
			BoolQueryBuilder bool = QueryBuilders.boolQuery();
			if(StringUtils.isNotEmpty(userId)){
				bool.must(QueryBuilders.termQuery("user_id", userId));
			}
			if(StringUtils.isNotEmpty(busType)){
				bool.must(QueryBuilders.termQuery("bus_type", busType));
			}
			if(StringUtils.isNotEmpty(busId)){
				bool.must(QueryBuilders.termQuery("bus_id", busId));
			}
			if(StringUtils.isNotEmpty(operator)){
				bool.must(QueryBuilders.termQuery("operator", operator));
			}
			if(status!=null) {
				bool.must(QueryBuilders.termQuery("action", status));
			}
			if(StringUtils.isNotEmpty(scoreFlag)){
				if(scoreFlag.equals("1")) {
					bool.must(QueryBuilders.rangeQuery("score").gte(0));
				}else if(scoreFlag.equals("0")) {
					bool.must(QueryBuilders.rangeQuery("score").lt(0));
				}
			}
			getCondition(bool,"update_time",adjustTimes,true);
			getCondition(bool,"valid_start_time",validTimes,true);
			String[] fields = {"user_id","bus_type", "bus_id", "score", "update_time", "valid_start_time", "valid_end_time", "action", "memo","operator"};
			FetchSourceContext sourceContext = new FetchSourceContext(true, fields, null);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.fetchSource(sourceContext);
			searchSourceBuilder.postFilter(bool);

			SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
					.setIndices(LoadValues.SCORE_DETAIL_INDEX)
					.setTypes(LoadValues.SCORE_DETAIL_INDEX)
					.setSource(searchSourceBuilder)
					.setFrom((page - 1) * size)
					.setSize(size)
					.setQuery(bool).addSort("update_time", SortOrder.DESC);
			SearchResponse response = requestBuilder.execute().actionGet();
			SearchHits hits = response.getHits();
			total = hits.getTotalHits();
			for (SearchHit hit : hits.getHits()) {
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				JSONObject object1 = new JSONObject();
				object1.put("userId", object.getString("user_id"));
				object1.put("busName", object.getString("bus_type"));
				object1.put("busId", object.getString("bus_id"));
				object1.put("score", object.getString("score"));
				object1.put("adjustTime", object.getString("update_time"));
				object1.put("validStartTime", object.getString("valid_start_time"));
				object1.put("validEndTime", object.getString("valid_end_time"));
				object1.put("status", getStatus(object.getIntValue("action")));
				object1.put("memo", object.getString("memo"));
				object1.put("operator", object.getString("operator"));
				newsArray.add(object1);
			}
			returnJson.put("items",newsArray);
			returnJson.put("total",total);
			NewsCache.getInstance().put(cacheKey,returnJson);
		}catch (Exception ex){
			returnJson.put("message",ex.toString());
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询信用明细列表所花时间:"+time);
		}
		return returnJson;
	}

	/**
	 * 获取状态
	 * @param status
	 * @return
	 */
	private String getStatus(int status){
		String returnStr = "";
		if(status == 0){
			returnStr = "正常";
		}else if(status == 1){
			returnStr = "异常";
		}
		return returnStr;
	}


	/**
	 * 保存调整数据
	 * @param map
	 * @throws Exception
	 */
	public String saveAjustData(Map<String,Object> map) throws Exception {
		String msg = "";
		try {
			logger.info("人工调整接收数据"+map);
			Map<String,Object> param = new HashMap<String,Object>();
			String userId = (String)map.get("userIds");
			String adjustScore = (String)map.get("adjustScore");
			String remark = (String)map.get("remark");
			String operator = (String)map.get("operator");
			String validStartTime = (String)map.get("validStartTime");
			String validEndTime = (String)map.get("validEndTime");
			param.put("adjustScore",adjustScore);
			param.put("remark",remark);
			param.put("validStartTime",validStartTime);
			param.put("validEndTime",validEndTime);
			param.put("productType","人工调整");
			param.put("operator",operator);
			if(StringUtils.isNotEmpty(userId)) {
				String[] userIdArray = userId.split(",");
				for(int i=0;i<userIdArray.length;i++) {
					param.put("userId",userIdArray[i]);
					CreditDataService.getInstance().save(param);
				}
			}
		}  catch (Exception e) {
			msg = e.getMessage();
			logger.error("BaDataService->saveAjustData:",e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}





	private Integer getScoreFre(int userId,boolean isAddScore,boolean isAjustScore) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId));
		if(isAjustScore) {
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("bus_type", "人工调整"));
		}else{
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("bus_type", "任务"));
		}
		if(isAddScore) {
			boolQueryBuilder.must(QueryBuilders.rangeQuery("score").gte(0));
		}else{
			boolQueryBuilder.must(QueryBuilders.rangeQuery("score").lt(0));
		}
		SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
				.setIndices(LoadValues.SCORE_DETAIL_INDEX)
				.setTypes(LoadValues.SCORE_DETAIL_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		Integer total = 0;
		try {
			response = requestBuilder.execute().get();
			total = ((Long)response.getHits().getTotalHits()).intValue();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return total;
	}

	private double getScore(int userId,boolean isAjustScore) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId));
		if(isAjustScore) {
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("bus_type", "人工调整"));
		}else{
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("bus_type", "任务"));
		}
		SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
				.setIndices(LoadValues.SCORE_DETAIL_INDEX)
				.setTypes(LoadValues.SCORE_DETAIL_INDEX)
				.setQuery(boolQueryBuilder).addAggregation(AggregationBuilders.sum("score").field("score"));
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			InternalSum agg = response.getAggregations().get("score");
			return formatDouble(agg.getValue());
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	private String formatDoubleStr(double param){
		return Double.parseDouble(df.format(param))*100+"%";
	}
	private double formatDouble(double param){
		return Double.parseDouble(df.format(param));
	}
}
