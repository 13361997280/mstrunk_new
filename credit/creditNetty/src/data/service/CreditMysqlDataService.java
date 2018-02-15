package data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.config.DbFactory;
import com.qbao.dao.BusScoreDetailMapper;
import com.qbao.dao.BusScoreMapper;
import com.qbao.dto.BusScore;
import com.qbao.dto.BusScoreDetail;
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

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CreditMysqlDataService {

	private static ESLogger logger = Loggers.getLogger(CreditMysqlDataService.class);
	private static CreditMysqlDataService service;
	private static TransportClient client;
	private static ExecutorService es;
	private static DecimalFormat df;
	private static SimpleDateFormat ddf;

	public static final CreditMysqlDataService getInstance(){
		try {
			if (service == null) {
				synchronized (CreditMysqlDataService.class) {
					if (service == null) {
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
						service = new CreditMysqlDataService();
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
			Date sysTime = new Date();
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

			String orderId = param.get("orderId")==null?null:param.get("orderId").toString();
			long amount = param.get("amount")==null?0:Long.parseLong(param.get("amount").toString());


			//计算逻辑
			/**-----------------业务信用总分初始化---------------**/

			BusScore oldRecord = getScore(userId);
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
			Date addTime = null;
			Date adjustTime = null;
			Date createTime = null;
			double score = 0;
			double signScore = 0;
			double taskScore = 0;
			int signFre = 0;
			if(oldRecord != null) {
				oldTotalScore = oldRecord.getTotalScore().doubleValue(); // 老的总分 从 es.getTotalScore(userId)
				oldAdjustScore = oldRecord.getAdjustScore().doubleValue();//老的调整总分 es.getAdjustScore(userId)
				creditScore = oldRecord.getCreditScore().doubleValue();
				addScore = oldRecord.getAddScore().doubleValue();
				taskScore = oldRecord.getTaskScore().doubleValue();
				addTime = oldRecord.getAddTime();
				adjustTime = oldRecord.getAdjustTime();
				createTime = oldRecord.getCreateTime();
				signScore = oldRecord.getSignScore().doubleValue();
				signFre = oldRecord.getSignFre();
				ratio = oldRecord.getRatio().doubleValue();
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
					double signTotalScore = signScore;
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
			BusScore busScore = new BusScore();
			busScore.setUserId(userId);
			busScore.setTotalScore(BigDecimal.valueOf(totalScore));
			busScore.setCreditScore(BigDecimal.valueOf(creditScore));
			busScore.setAddScore(BigDecimal.valueOf(addScore));
			busScore.setAdjustScore(BigDecimal.valueOf(adjustScore));
			busScore.setAddTime(addTime);
			busScore.setStatus(status);
			busScore.setAdjustTime(adjustTime);
			busScore.setCreateTime(createTime);
			busScore.setUpdateTime(sysTime);
			busScore.setSignScore(BigDecimal.valueOf(signScore));
			busScore.setTaskScore(BigDecimal.valueOf(taskScore));
			busScore.setSignFre(signFre);
			busScore.setRatio(BigDecimal.valueOf(ratio));

			busId = RedisUnit.getBusId(busType);
			BusScoreDetail busScoreDetail = new BusScoreDetail();
			busScoreDetail.setUserId(userId);
			busScoreDetail.setProductId(productId);
			busScoreDetail.setTaskId(taskId);
			busScoreDetail.setCompanyId(companyId);
			busScoreDetail.setBusId(busId);
			busScoreDetail.setBusType(busType);
			busScoreDetail.setOperator(operator);
			busScoreDetail.setScore(BigDecimal.valueOf(score));
			if(StringUtils.isNotEmpty(validStartTime)) {
				if(validStartTime.length()==19){
					validStartTime = validStartTime+".000";
				}
				busScoreDetail.setValidStartTime(df.parse(validStartTime));
			}
			if(StringUtils.isNotEmpty(validEndTime)) {
				if(validEndTime.length()==19){
					validEndTime = validEndTime+".999";
				}
				busScoreDetail.setValidEndTime(df.parse(validEndTime));
			}
			busScoreDetail.setMemo(memo);
			busScoreDetail.setSource(0);//ba后台业务方
			busScoreDetail.setOrderId(orderId);
			busScoreDetail.setAmount(BigDecimal.valueOf(amount));
			busScoreDetail.setQuantity(quantity);
			busScoreDetail.setcOrderId("");
			busScoreDetail.setAction(action);
			busScoreDetail.setCreateTime(sysTime);
			busScoreDetail.setUpdateTime(sysTime);

			BusScoreMapper busScoreMapper = DbFactory.createBean(BusScoreMapper.class);
			if(oldRecord==null) {
				busScoreMapper.insertSelective(busScore);
			}else{
				busScoreMapper.updateByUserIdSelective(busScore);
			}

			//保存明细表
			BusScoreDetailMapper busScoreDetailMapper = DbFactory.createBean(BusScoreDetailMapper.class);
			if(busType.equals("签到")){
				BusScoreDetail tempBusScoreDetail = busScoreDetailMapper.selectByUserId(userId);
				if(tempBusScoreDetail==null){
					busScoreDetailMapper.insertSelective(busScoreDetail);
				}else{
					busScoreDetailMapper.updateByUserIdSelective(busScoreDetail);
				}
			}else{
				busScoreDetailMapper.insertSelective(busScoreDetail);
			}

			String keyValue = String.format(RedisConstant.CREDIT_TOTAL_SCORE_USER.getKey(),userId);
			redis.RedisUtil.getInstance().del(keyValue);


		}  catch (Exception e) {
			logger.error("CreditDataService.save error ", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errorMsg;

	}

	private  BusScore getScore(int userId) {
		BusScoreMapper busScoreMapper = DbFactory.createBean(BusScoreMapper.class);
		return busScoreMapper.selectByUserId(userId);
	}
	private double getTodayScore(int userId) {
		BusScoreDetailMapper mapper = DbFactory.createBean(BusScoreDetailMapper.class);
		BusScoreDetail busScoreDetail = new BusScoreDetail();
		busScoreDetail.setUserId(userId);
		busScoreDetail.setUpdateTime(new Date());
		Double returnValue = mapper.todayScore(busScoreDetail);
		if(returnValue==null) return 0;
		return returnValue;
	}

	/**
	 * 获取该用户今天的任务总分
	 * @param userId
	 * @return
     */
	private double getTodayTaskScore(int userId) {
		BusScoreDetailMapper mapper = DbFactory.createBean(BusScoreDetailMapper.class);
		BusScoreDetail busScoreDetail = new BusScoreDetail();
		busScoreDetail.setUserId(userId);
		busScoreDetail.setUpdateTime(new Date());
		busScoreDetail.setProductId(10102);
		Double returnValue = mapper.todayScore(busScoreDetail);
		if(returnValue==null) return 0;
		return returnValue;
	}

	/**
	 * 获取该用户领取的任务总分
	 * @param userId
	 * @return
     */
	private double getTaskTotalScore(int userId) {
		BusScoreDetailMapper mapper = DbFactory.createBean(BusScoreDetailMapper.class);
		BusScoreDetail busScoreDetail = new BusScoreDetail();
		busScoreDetail.setUserId(userId);
		busScoreDetail.setProductId(10102);
		Double returnValue = mapper.todayScore(busScoreDetail);
		if(returnValue==null) return 0;
		return returnValue;
	}

	/**---------------------------------补充方法逻辑---------------------------------**/
	/**
	 * 用户今天是否签到
	 * @param userId
	 * @return
     */
	public boolean isTodaySign(int userId) {
		BusScoreDetailMapper mapper = DbFactory.createBean(BusScoreDetailMapper.class);
		BusScoreDetail busScoreDetail = new BusScoreDetail();
		busScoreDetail.setUserId(userId);
		busScoreDetail.setUpdateTime(new Date());
		busScoreDetail.setProductId(10101);
		return mapper.isExist(busScoreDetail);
	}

	/**
	 * 判断用户是否第一次领该任务
	 * @param userId
	 * @param taskId
     * @return
     */
	public boolean isFirstFetchTheTask(int userId,int taskId) {
		BusScoreDetailMapper mapper = DbFactory.createBean(BusScoreDetailMapper.class);
		BusScoreDetail busScoreDetail = new BusScoreDetail();
		busScoreDetail.setUserId(userId);
		busScoreDetail.setTaskId(taskId);
		return mapper.isExist(busScoreDetail);
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
			userScore = JSON.parseObject(obj).getDoubleValue("totalScore");
		}else {
			BusScoreMapper mapper = DbFactory.createBean(BusScoreMapper.class);
			BusScore entity = mapper.selectByUserId(userId);
			if(entity != null){
				userScore = entity.getTotalScore().doubleValue();
				redis.RedisUtil.getInstance().set(keyValue, JSON.toJSONString(entity), DateUtil.getRedisExpired().intValue());
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
			BusScoreMapper mapper = DbFactory.createBean(BusScoreMapper.class);
			BusScore entity = mapper.selectByUserId(userId);
			if(entity != null){
				userScore = entity.getTotalScore().doubleValue();
				status = entity.getStatus();
				redis.RedisUtil.getInstance().set(keyValue, JSON.toJSONString(entity), DateUtil.getRedisExpired().intValue());
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
