package data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import redis.RedisConstant;
import util.DateUtil;

import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class CreditDataService {

	private static ESLogger logger = Loggers.getLogger(CreditDataService.class);
	private static CreditDataService service;
	private static TransportClient client;
	private static ExecutorService es;
	private static DecimalFormat df;
	private static SimpleDateFormat ddf;

	public static final CreditDataService getInstance(){
		try {
			if (service == null) {
				synchronized (CreditDataService.class) {
					if (service == null) {
						//-----------------es集群连接--------------------
						Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
								.put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证, 打开后集群名字不对也能连接上
								.build();
						client = new PreBuiltTransportClient(settings)
								.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(Config.get().get("es.addr"), Config.get().getInt("es.port", 9300))));
						es = new ThreadPoolExecutor(
								Config.get().getInt("server.boss.executor.core.pool.size",
										Runtime.getRuntime().availableProcessors() * 2),
								Config.get().getInt("server.boss.executor.max.pool.size",
										10000),
								60L,
								TimeUnit.SECONDS,
								new LinkedBlockingQueue<Runnable>(500),
								new ThreadPoolExecutor.CallerRunsPolicy()
						);
						service = new CreditDataService();
						df = new DecimalFormat("######0.00");
						ddf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return service;
	}
	public Client getClient() {
		return client;
	}
	public ExecutorService getExecutorService() {
		return es;
	}

	/**
	 * 业务逻辑处理
	 * @throws Exception
	 */
	public void doProcess(Map<String,Object> param) throws Exception {
		Queue.offer(param);
	}
	/**
	 * 保存分
	 * @throws Exception
	 */
	public String save(Map<String,Object> param) throws Exception {
		logger.info("CreditDataService->save",param);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String errorMsg = "成功";
		try {
			/**-----------------参数初始化---------------**/
			String sysTime = df.format(new Date());
			String timestamp = param.get("timestamp")==null?System.currentTimeMillis()+"":param.get("timestamp").toString();
			int    companyId = param.get("companyId")==null?0:Integer.parseInt(param.get("companyId").toString());
			int    action = param.get("action")==null?0:Integer.parseInt(param.get("action").toString());
			int    quantity = param.get("quantity")==null?1:Integer.parseInt(param.get("quantity").toString());
			int    userId = param.get("userId")==null?0:Integer.parseInt(param.get("userId").toString());
			int    productId = param.get("productId")==null?0:Integer.parseInt(param.get("productId").toString());
			int    taskId = param.get("taskId")==null?0:Integer.parseInt(param.get("taskId").toString());
			String busType = param.get("productType")==null?"":param.get("productType").toString();
			String operator = param.get("operator")==null?"":param.get("operator").toString();
			String memo = param.get("remark")==null?null:param.get("remark").toString();
			String validStartTime = param.get("validStartTime")==null?null:param.get("validStartTime").toString();
			String validEndTime = param.get("validEndTime")==null?null:param.get("validEndTime").toString();


			//计算逻辑
			/**-----------------业务信用总分初始化---------------**/

			JSONObject oldRecord = getScore(userId);
			double totalScore = RedisUnit.getTotalScore();
			double oldTotalScore = 0;
			double oldAdjustScore = 0;
			double ratio = 0;
			int    busId = 0;
			long   version = 0;
			int    status = 0; //总分升降标识  0:正常 1:升 -1:降

			double creditScore = RedisUnit.getCreditScore();
			double addScore = 0;
			double adjustScore = 0;
			String addTime = null;
			String adjustTime = null;
			String createTime = null;
			double score = 0;
			double signScore = 0;
			double taskScore = 0;
			double signFre = 0;
			if(oldRecord != null) {
				version = oldRecord.getLongValue("_version");
				oldTotalScore = oldRecord.getDoubleValue("total_score"); // 老的总分 从 es.getTotalScore(userId)
				oldAdjustScore = oldRecord.getDoubleValue("adjust_score");//老的调整总分 es.getAdjustScore(userId)
				creditScore = oldRecord.getDoubleValue("credit_score");
				addScore = oldRecord.getDoubleValue("add_score");
				taskScore = oldRecord.getDoubleValue("task_score");
				addTime = oldRecord.getString("add_time");
				adjustTime = oldRecord.getString("adjust_time");
				createTime = oldRecord.getString("create_time");
				signScore = oldRecord.getDoubleValue("sign_score");
				signFre = oldRecord.getDoubleValue("sign_fre");
				ratio = oldRecord.getDoubleValue("ratio");
			}else{
				createTime = sysTime;
			}


			/**-----------------业务逻辑处理---------------**/
			if(busType.equals("任务")) {
				double taskAddScore = RedisUnit.getTaskAddScore(taskId);
				double taskSubScore = RedisUnit.getTaskSubScore(taskId);
				double taskScoreConf = action == 0? taskAddScore:-taskSubScore;//任务可加分数 手动结束任务为负数,否则正数
				double tmpTaskScore = 0;
				addTime = sysTime;
				String isDuplicate = RedisUnit.getIsDuplicate(taskId);// 任务是否重复加分 Y:可重复 N:不可重复
				double currentTaskScore = taskScoreConf; // 任务分
				if (isDuplicate.equals("Y")) {
					currentTaskScore = taskScoreConf * quantity;
				}else if (isDuplicate.equals("N")){
					boolean isFirsFetchTheTask = isFirstFetchTheTask(userId,taskId);
					if(isFirsFetchTheTask){
						currentTaskScore = taskScoreConf;
					}else{
						currentTaskScore = 0;
						memo = "领任务-不能重复加分";
					}
				}
				if (currentTaskScore > 0) {
					double todayScore = getTodayScore(userId);// 单日总分
					tmpTaskScore = currentTaskScore + todayScore;
					double todayScoreLimit = RedisUnit.getTodayScoreLimit();//单日总上限分
					if (tmpTaskScore > todayScoreLimit && todayScoreLimit >= 0) {
						currentTaskScore =  todayScoreLimit - todayScore;
						memo = "领任务-超过配置上限";
					}
					if(currentTaskScore <= 0){
						currentTaskScore = 0;
					}else{
						currentTaskScore = formatDouble(currentTaskScore);
					}
					if (currentTaskScore > 0) {
						double taskTotalScore = getTaskTotalScore(userId);//任务总分
						tmpTaskScore = currentTaskScore + taskTotalScore;
						double taskScoreLimit = RedisUnit.getTaskScoreLimit();//任务总上限分
						if (tmpTaskScore > taskScoreLimit && taskScoreLimit >= 0) {
							currentTaskScore = taskScoreLimit - taskTotalScore;
							memo = "领任务-超过配置上限";
						}
					}
					if(currentTaskScore <= 0){
						currentTaskScore = 0;
					}else{
						currentTaskScore = formatDouble(currentTaskScore);
					}
					if (currentTaskScore > 0) {
						double todayTaskScore = getTodayTaskScore(userId);//单日任务总分
						tmpTaskScore = currentTaskScore + todayTaskScore;
						double todayTaskScoreLimit = RedisUnit.getTodayTaskScoreLimit();//单日任务总上限分
						if (tmpTaskScore > todayTaskScoreLimit && todayTaskScoreLimit >= 0) {
							currentTaskScore = todayTaskScoreLimit - todayTaskScore;
							memo = "领任务-超过配置上限";
						}
					}
				}
				if(currentTaskScore <= 0){
					currentTaskScore = 0;
				}else{
					currentTaskScore = formatDouble(currentTaskScore);
				}
				taskScore = taskScore + currentTaskScore;
				addScore = signScore + taskScore;
				totalScore = addScore + oldAdjustScore + creditScore;

				score = currentTaskScore;
				adjustScore = oldAdjustScore;
				if(memo==null){
					memo = "领任务";
					if(action==1){
						memo = "手动终止任务";
					}
				}
			}else if(busType.equals("签到")){
				if(RedisUnit.isSign(userId)) {
					errorMsg = "用户今天已经签到";
					logger.info(errorMsg);
					return errorMsg;
				}
				signFre += 1;
				addTime = sysTime;
				double signScoreConf = RedisUnit.getSignScore();
				double todayScoreLimit = RedisUnit.getTodayScoreLimit();//单日总上限分
				double todayScore = getTodayScore(userId);
				double currentSignScore = signScoreConf;
				double tmpSignScore = currentSignScore + todayScore;
				if (tmpSignScore > todayScoreLimit && todayScoreLimit >= 0) { // 单日总上限分
					currentSignScore = todayScoreLimit - todayScore;
					memo = "签到-超过配置上限";
				}
				if(currentSignScore < 0){
					currentSignScore = 0;
				}else{
					currentSignScore = formatDouble(currentSignScore);
				}
				if(currentSignScore > 0) {
					double signTotalScore = getSignTotalScore(userId);
					tmpSignScore = currentSignScore + signTotalScore;
					double signScoreLimit = RedisUnit.getSignScoreLimit();//单日总上限分
					if (tmpSignScore > signScoreLimit && signScoreLimit >= 0) { // 签到总上限分
						currentSignScore = signScoreLimit - signTotalScore;
						memo = "签到-超过配置上限";
					}
				}
				if(currentSignScore < 0){
					currentSignScore = 0;
				}else{
					currentSignScore = formatDouble(currentSignScore);
				}
				signScore = signScore + currentSignScore;
				addScore = taskScore + signScore;
				totalScore =  addScore + oldAdjustScore + creditScore;
				//ratio = RedisUtil.getRatio(totalScore)[0]; //redis.getRatio(totalScore)
				score = currentSignScore;
				adjustScore = oldAdjustScore;
				logger.info("CreditDataService->log:进redis userId:"+userId);
				String keyValue = String.format(RedisConstant.CREDIT_CONF_SIGN_FLAG.getKey(),userId);
				redis.RedisUtil.getInstance().set(keyValue,1+"", DateUtil.getRedisExpired().intValue());
				if(memo==null){
					memo = "签到";
				}
			}else if(busType.equals("人工调整")){
				adjustTime = sysTime;
				adjustScore = param.get("adjustScore")==null?0:Double.parseDouble(param.get("adjustScore").toString());
				score = adjustScore;
				adjustScore = adjustScore + oldAdjustScore;
				totalScore =  creditScore + addScore + adjustScore;//总分 = 信用分 + 加分项总分 + 调整分
				//ratio = RedisUtil.getRatio(totalScore)[0];
				if(memo==null){
					memo = "人工调整加分";
					if(adjustScore < 0){
						memo = "人工调整减分";
					}
				}
			}
			ratio = Double.parseDouble(RedisUnit.getRatio(totalScore).get("currentRatio").toString());
			if(totalScore > oldTotalScore){
				status = 1;
			}else if(totalScore < oldTotalScore){
				status = -1;
			}
			/**-----------------业务存储逻辑处理---------------**/
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user_id",userId);
			//map.put("ratio",ratio);
			//map.put("next_ratio",RedisUtil.getRatio(totalScore)[1]);
			map.put("total_score",totalScore);
			map.put("credit_score",creditScore);
			map.put("add_score",addScore);
			map.put("adjust_score",adjustScore);
			map.put("add_time",addTime);
			map.put("status",status);
			map.put("adjust_time",adjustTime);
			map.put("create_time",createTime);
			map.put("update_time",sysTime);
			map.put("sign_score",signScore);
			map.put("task_score",taskScore);
			map.put("sign_fre",signFre);
			map.put("ratio",ratio);

			busId = RedisUnit.getBusId(busType);
			Map<String,Object> detailMap = new HashMap<String,Object>();
			detailMap.put("user_id",userId);
			detailMap.put("product_id",productId);
			detailMap.put("task_id",taskId);
			detailMap.put("company_id",companyId);
			detailMap.put("bus_id",busId);
			detailMap.put("bus_type",busType);
			detailMap.put("operator",operator);
			detailMap.put("score",score);
			if(StringUtils.isNotEmpty(validStartTime)) {
				if(validStartTime.length()==19){
					validStartTime = validStartTime+".000";
				}
				detailMap.put("valid_start_time", validStartTime);
			}
			if(StringUtils.isNotEmpty(validEndTime)) {
				if(validEndTime.length()==19){
					validEndTime = validEndTime+".999";
				}
				detailMap.put("valid_end_time", validEndTime);
			}
			detailMap.put("memo",memo);
			detailMap.put("source",0);//ba后台业务方
			//detailMap.put("order_id",0);
			//detailMap.put("amount",0);
			detailMap.put("quantity",quantity);
			//detailMap.put("c_order_id",cOrderId);
			detailMap.put("action",action);
			detailMap.put("create_time",sysTime);
			detailMap.put("update_time",sysTime);

			//保存总分表
			String keyId = userId+"";
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(LoadValues.SCORE_INDEX, LoadValues.SCORE_INDEX)
					.setId(keyId).setSource(map);
			if(version > 0){
				indexRequestBuilder.setVersion(version);
			}
			bulkRequest.add(indexRequestBuilder);
			bulkRequest.execute();
			//保存明细表
			keyId = timestamp + "-" + UUID.randomUUID();
			if(busType.equals("签到")){
				keyId = userId+"";
			}
			bulkRequest = client.prepareBulk();
			bulkRequest.add(client.prepareIndex(LoadValues.SCORE_DETAIL_INDEX, LoadValues.SCORE_DETAIL_INDEX)
					.setId(keyId).setSource(detailMap));
			bulkRequest.execute();
			String keyValue = String.format(RedisConstant.CREDIT_TOTAL_SCORE_USER.getKey(),userId);
			redis.RedisUtil.getInstance().del(keyValue);

			//调用接口保存明细和总分到log系统中
			boolean logFlag = Config.get().getBoolean("log.flag",false);
			if(logFlag) {
				JSONObject toLog = new JSONObject();
				toLog.put("companyId", 101);
				toLog.put("productId", 10114);
				toLog.put("pageId", UUID.randomUUID());
				toLog.put("uid", userId);
				toLog.put("stamp", System.currentTimeMillis());
				toLog.put("data", JSON.toJSONString(map));
				Map<String, Object> logMap = new HashMap<String, Object>();
				logMap.put("log", toLog.toJSONString());
				toLog.put("productId", 10115);
				toLog.put("pageId", UUID.randomUUID());
				toLog.put("stamp", System.currentTimeMillis());
				toLog.put("data", JSON.toJSONString(detailMap));
				logMap.put("detailLog", toLog.toJSONString());
				logger.info("信用明细进中央日志系统队列");
				Queue.logOffer(logMap);
			}

		}  catch (Exception e) {
			logger.error("CreditDataService.save error ", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorMsg;

	}
	public void logSave(Map<String,Object> param) {
		try {
			String log = param.get("log").toString();
			String detailLog = param.get("detailLog").toString();
			String url = Config.get().get("logcenter.url") == null ? "http://logs.qbao.com/compass/log" : Config.get().get("logcenter.url");
			HttpUtils.postByJson1(url, log);
			HttpUtils.postByJson1(url, detailLog);
		}catch (Exception ex){
			logger.error("CreditDataService->logSave error:",ex);
		}

	}
	private double getSignTotalScore(int userId) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SCORE_INDEX)
				.setTypes(LoadValues.SCORE_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()==0) return 0;
			SearchHit hit = response.getHits().getHits()[0];
			JSONObject object = JSON.parseObject(hit.getSourceAsString());
			return object.getDoubleValue("sign_score");
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	private  JSONObject getScore(int userId) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("_id",userId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SCORE_INDEX)
				.setTypes(LoadValues.SCORE_INDEX)
				.setQuery(boolQueryBuilder).setVersion(true);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()==0)return null;
			SearchHit hit = response.getHits().getHits()[0];
			JSONObject object = JSON.parseObject(hit.getSourceAsString());
			object.put("_version",hit.getVersion());
			return object;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	private double getTodayScore(int userId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sysTime = df.format(new Date());
		String startTime = sysTime+" 00:00:00.000";
		String endTime = sysTime+" 23:59:59.999";
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId))
				.must(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("bus_type","签到")).should(QueryBuilders.termQuery("bus_type","任务")))
				.must(QueryBuilders.rangeQuery("update_time").gte(startTime).lte(endTime));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
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

	/**
	 * 获取该用户今天的任务总分
	 * @param userId
	 * @return
     */
	private double getTodayTaskScore(int userId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sysTime = df.format(new Date());
		String startTime = sysTime+" 00:00:00.000";
		String endTime = sysTime+" 23:59:59.999";
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId))
				.must(QueryBuilders.termQuery("bus_type","任务"))
				.must(QueryBuilders.rangeQuery("update_time").gte(startTime).lte(endTime));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SCORE_DETAIL_INDEX)
				.setTypes(LoadValues.SCORE_DETAIL_INDEX)
				.setQuery(boolQueryBuilder).addAggregation(AggregationBuilders.sum("score").field("score"));
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			InternalSum agg = response.getAggregations().get("score");
			return agg.getValue();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取该用户领取的任务总分
	 * @param userId
	 * @return
     */
	private double getTaskTotalScore(int userId) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId))
				.must(QueryBuilders.termQuery("bus_type","任务"));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
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

	/**---------------------------------补充方法逻辑---------------------------------**/
	/**
	 * 用户今天是否签到
	 * @param userId
	 * @return
     */
	public boolean isTodaySign(int userId) {
		boolean isSign = true;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sysTime = df.format(new Date());
		String startTime = sysTime+" 00:00:00.000";
		String endTime = sysTime+" 23:59:59.999";
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId))
				.must(QueryBuilders.termQuery("bus_type","签到"))
				.must(QueryBuilders.rangeQuery("update_time").gte(startTime).lte(endTime));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SCORE_DETAIL_INDEX)
				.setTypes(LoadValues.SCORE_DETAIL_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()==0){
				isSign = false;
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return isSign;
	}

	/**
	 * 判断用户是否第一次领该任务
	 * @param userId
	 * @param taskId
     * @return
     */
	public boolean isFirstFetchTheTask(int userId,int taskId) {
		boolean isFirstFetchTheTask = true;
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId))
				.must(QueryBuilders.termQuery("task_id",taskId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SCORE_DETAIL_INDEX)
				.setTypes(LoadValues.SCORE_DETAIL_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()==0){
				isFirstFetchTheTask = false;
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return isFirstFetchTheTask;
	}

	/**
	 * 信用总分及系数接口
	 */
	public JSONObject getTotalScore(Integer userId) {
		String keyValue = String.format(RedisConstant.CREDIT_TOTAL_SCORE_USER.getKey(),userId);
		String obj = redis.RedisUtil.getInstance().get(keyValue);
		double userScore = RedisUnit.getTotalScore();
		JSONObject map = new JSONObject();
		if(StringUtils.isNotEmpty(obj)){
			userScore = JSON.parseObject(obj).getDoubleValue("total_score");
		}else {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id", userId));
			SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
					.setIndices(LoadValues.SCORE_INDEX)
					.setTypes(LoadValues.SCORE_INDEX)
					.setQuery(boolQueryBuilder);
			try {
				SearchResponse response = requestBuilder.execute().get();
				if (response.getHits().getTotalHits() > 0) {
					SearchHit hit = response.getHits().getHits()[0];
					JSONObject object = JSON.parseObject(hit.getSourceAsString());
					userScore = object.getDoubleValue("total_score");
					redis.RedisUtil.getInstance().set(keyValue, object.toJSONString(), DateUtil.getRedisExpired().intValue());
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		map.put("creditScore", userScore);
		map.put("ratio", RedisUnit.getRatio(userScore).get("currentRatio"));
		return map;
	}

	/**
	 * 信用系数详细接口
	 */
	public JSONObject getRatio(Integer userId) {
		String keyValue = String.format(RedisConstant.CREDIT_TOTAL_SCORE_USER.getKey(),userId);
		String obj = redis.RedisUtil.getInstance().get(keyValue);
		String sysTime = ddf.format(new Date());
		JSONObject map = new JSONObject();
		double userScore = RedisUnit.getTotalScore();
		int status = 0;
		if(StringUtils.isNotEmpty(obj)){
			userScore = JSON.parseObject(obj).getDoubleValue("total_score");
			status = JSON.parseObject(obj).getIntValue("status");
		}else {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id", userId));
			SearchRequestBuilder requestBuilder = CreditDataService.getInstance().getClient().prepareSearch()
					.setIndices(LoadValues.SCORE_INDEX)
					.setTypes(LoadValues.SCORE_INDEX)
					.setQuery(boolQueryBuilder);
			try {

				SearchResponse response = requestBuilder.execute().get();
				SearchHits searchHits = response.getHits();
				if (searchHits.getTotalHits() > 0) {
					SearchHit hit = response.getHits().getHits()[0];
					JSONObject object = JSON.parseObject(hit.getSourceAsString());
					userScore = object.getDoubleValue("total_score");
					status = object.getIntValue("status");
					redis.RedisUtil.getInstance().set(keyValue, object.toJSONString(), DateUtil.getRedisExpired().intValue());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		map.put("nextRatio", RedisUnit.getRatio(userScore).get("nextRatio"));
		map.put("sort", status);
		map.put("todayScore", getTodayScore(userId));
		map.put("availableScore", RedisUnit.getTodayScoreLimit());
		map.put("ratio", RedisUnit.getRatio(userScore).get("currentRatio"));
		map.put("updateTime", sysTime);
		boolean isSign = RedisUnit.isSign(userId);
		Integer signFlag = isSign ? 1 : 0;
		map.put("items", RedisUnit.getItems(signFlag));
		return map;
	}

	private  double formatDouble(double param){
		return Double.parseDouble(df.format(param));
	}

	public static void main(String[] args){
	}
}
