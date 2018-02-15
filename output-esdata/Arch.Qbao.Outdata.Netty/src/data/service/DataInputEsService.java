package data.service;

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSON;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import kafka.KafkaProduce;
import util.DateUtil;
import vo.QbaoLog;
import vo.TaskRecord;

public class DataInputEsService {

	private static ESLogger logger = Loggers.getLogger(DataInputEsService.class);
	private static DataInputEsService dataInputEsService;
	private static TransportClient client;

	public static final DataInputEsService getInstance() {
		try {
			if (dataInputEsService == null) {
				synchronized (PoiEsDataService.class) {
					// -----------------es集群连接--------------------
					Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
							.put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证,
																				// 打开后集群名字不对也能连接上
							.build();
					client = new PreBuiltTransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(
									Config.get().get("es.host1"), Config.get().getInt("es.port", 9300))))
							.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(
									Config.get().get("es.host2"), Config.get().getInt("es.port", 9300))))
							.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(
									Config.get().get("es.host3"), Config.get().getInt("es.port", 9300))));
					dataInputEsService = new DataInputEsService();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dataInputEsService;
	}

	/**
	 * LBS数据导入kafka
	 * 
	 * @return
	 */
	public String dealLbsDataInput() {
		// 1.ES 定时任务操作日志表里拉取最近一次数据，获取上次操作的最后时间。
		String lastTime = getTaskRecodeByType(LoadValues.LABEL_LBS);
		/**
		 * 2.根据以上时间，加当前时间。拉取对应的数据日志。
		 * 3.拉取时间段数据，判断总条数，设置默认5000条拉取一次。如果超过最大值，根据总条数计算分页，循环获取 4.拉取推送完毕，更新ES
		 * 操作记录
		 */
//		lastTime = "2017-08-15 00:21:54";
		String endTimeStr = DateUtil.getFormatDate(new Date(), DateUtil.DATATIMEF_STR);
		return getJsonObjectStrForConst(lastTime, endTimeStr, LoadValues.LABEL_LBS, LoadValues.LABEL_LBS, LoadValues.LBS_ES_FILTER_QUERY, "update");
	}
	
	/**
	 * qbaoLog数据导入kafka
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public String dealQbaoLogDataInput(Long start, Long end) {
		// 1.ES 定时任务操作日志表里拉取最近一次数据，获取上次操作的最后时间。
		String lastTime = getTaskRecodeByType(LoadValues.LABEL_QBAOLOG);
		Long lstLong = null;
		if (StringUtils.isNotBlank(lastTime)) {
			try {
				 lstLong = DateUtil.StringToLong(lastTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long endTime = System.currentTimeMillis();
		if (start > 0) {
			lstLong = start;
		}
		if (end > 0) {
			endTime = end;
		}
		return getOutAllData(lstLong, endTime, LoadValues.LABEL_QBAOLOG, LoadValues.LABEL_QBAOLOG, "", "stamp");
	}
	
	/**
	 * toutiao数据导入kafka
	 * 
	 * @return
	 */
	public String dealTouTiaoDataInput() {
		// 1.ES 定时任务操作日志表里拉取最近一次数据，获取上次操作的最后时间。
		String lastTime = getTaskRecodeByType(LoadValues.LABEL_TOUTIAO);
//		lastTime = "2017-08-18 00:21:54";
		String endTimeStr = DateUtil.getFormatDate(new Date(), DateUtil.DATATIMEF_STR);
		return getJsonObjectStrForConst(lastTime, endTimeStr, LoadValues.LABEL_TOUTIAO, LoadValues.LABEL_TOUTIAO, LoadValues.TOUTIAO_ES_FILTER_QUERY, "crawl_date_time");
	}

	/**
	 * 查询日志操作表数据
	 * type 1. lbs 2.oneDayLog 3.toutiao
	 */
	public String getTaskRecodeByType(String type) {
		String object = null;
		try {
			BoolQueryBuilder qb =  QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("type", type));
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(LoadValues.LABEL_TASKRECORD).setTypes(LoadValues.LABEL_TASKRECORD);
			
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			String fields = "operate_time";
			String[] fieldArray = fields.split(",");
			FetchSourceContext sourceContext = new FetchSourceContext(true,fieldArray,null);
			searchSourceBuilder.fetchSource(sourceContext);
			searchSourceBuilder.postFilter(qb);
			searchRequestBuilder.setSource(searchSourceBuilder).setQuery(qb).addSort("operate_time", SortOrder.DESC).setSize(1);
			SearchResponse response = searchRequestBuilder
					.execute().actionGet();
			SearchHit[] hits = response.getHits().getHits();
			if(hits.length==0) return null;
			object = hits[0].getSourceAsString();
			TaskRecord taskRecord = JSON.parseObject(object, TaskRecord.class);
			if(null != taskRecord) {
				object = taskRecord.getOperate_time();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return object;
	}

	/**
	 * 删除es数据
	 */
	public void delete(String indexName, String type) {
		try {
			BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
					.filter(QueryBuilders.matchQuery("_type", type)).source(indexName).get();
			long deleted = response.getDeleted();
			logger.info("EsDataService.delete->count", deleted + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据时间查询es表中数据 type 1. lbs 2.oneDayLog 3.toutiao return 1:success 0:false
	 */
	public String getJsonObjectStrForConst(Object fromTime, Object endTime, String indexName, String type,
			String fields, String queryIndex) {
		Long totalNum = 0L;
		try {
			BoolQueryBuilder qb = QueryBuilders.boolQuery();

			if (fromTime == null) {
				qb.must(QueryBuilders.rangeQuery(queryIndex).lt(endTime).includeUpper(true));
			} else {
				qb.must(QueryBuilders.rangeQuery(queryIndex).gt(fromTime).lt(endTime).includeLower(false)
						.includeUpper(true));
			}
			
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes(type);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			if (fields != null && !"".equals(fields)) {
				String[] fieldArray = fields.split(",");
				FetchSourceContext sourceContext = new FetchSourceContext(true, fieldArray, null);
				searchSourceBuilder.fetchSource(sourceContext);
			}
			searchSourceBuilder.postFilter(qb);
			int page = 0;
			int size = 1000;
			searchRequestBuilder.setSource(searchSourceBuilder).addSort("_uid", SortOrder.ASC).setFrom((page) * size).setSize(size);
			SearchResponse response = searchRequestBuilder.execute().actionGet();
			System.out.println(response.getHits().getTotalHits());
			SearchHit[] hits = response.getHits().getHits();
			if (hits.length == 0) {
				return "fromTime=" + fromTime + ",endTime=" + endTime + ",topic=" + type + ",count=0";
			} else {
				// 发送数据到kafka
				sendKafka(hits, indexName);
			}			
			totalNum = response.getHits().getTotalHits();
			// 根据总数，分页查询
			if (totalNum > size) {
				try {
					boolean flag = true;
					while (flag) {
						searchRequestBuilder.setSource(searchSourceBuilder).addSort("_uid", SortOrder.ASC).setFrom((page + 1) * size).setSize(size);
						response = searchRequestBuilder.execute().actionGet();
						hits = response.getHits().getHits();
						if (null == hits || hits.length == 0) {
							flag = false;
							break;
						}
						page++;
						flag = true;
						// 发送数据到kafka
						sendKafka(hits, indexName);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("发送kafka失败", e.getMessage());
				}
			}
			// 顺利执行完成 跟新recordtask数据
			Date endTimeDate = new Date();
			if (endTime instanceof String) {
				endTimeDate = DateUtil.stringToDate(endTime.toString(), DateUtil.DATATIMEF_STR);
			} else if (endTime instanceof Long){
				endTimeDate = DateUtil.LongToDate(((Long) endTime).longValue());
			}
			addRecordTask(type, totalNum, endTimeDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "fromTime=" + fromTime + ",endTime=" + endTime + ",topic=" + type + ",count=" + totalNum;
	}
	
	/**
	 * 全量导出
	 */
	public String getOutAllData(Object fromTime, Object endTime, String indexName, String type, String fields,
			String queryIndex) {
		Long totalNum = 0L;
		try {
			SearchResponse response = client.prepareSearch(indexName).setTypes(type)
					.setQuery(QueryBuilders.matchAllQuery()).setSize(10000).setScroll(new TimeValue(600000))
					.setSearchType(SearchType.DEFAULT).execute().actionGet();
			// setSearchType(SearchType.Scan) 告诉ES不需要排序只要结果返回即可 setScroll(new
			// TimeValue(600000)) 设置滚动的时间
			String scrollid = response.getScrollId();
			totalNum = response.getHits().getTotalHits();
			while (true) {
				Thread.sleep(5000);
				// 发送数据到kafka
				sendKafka(response.getHits().getHits(), indexName);
				response = client.prepareSearchScroll(scrollid).setScroll(new TimeValue(1000000)).execute().actionGet();
				// 再次查询不到数据时跳出循环
				SearchHit[] hits = response.getHits().getHits();
				if (hits.length == 0) {
					break;
				}
			}
			System.out.println(response.getHits().getTotalHits());
			// 顺利执行完成 跟新recordtask数据
			Date endTimeDate = new Date();
			if (endTime instanceof String) {
				endTimeDate = DateUtil.stringToDate(endTime.toString(), DateUtil.DATATIMEF_STR);
			} else if (endTime instanceof Long) {
				endTimeDate = DateUtil.LongToDate(((Long) endTime).longValue());
			}
			addRecordTask(type, totalNum, endTimeDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "fromTime=" + fromTime + ",endTime=" + endTime + ",topic=" + type + ",count=" + totalNum;
	}
	
	/**
	 * kafka调用
	 * 
	 */
	public Boolean sendKafka(SearchHit[] hits, String topic) {
		if (null == hits || hits.length <= 0) {
			return false;
		}
		logger.info("kafka发送开始： topic = {}, count={}", topic, hits.length);
		KafkaProduce producer = KafkaProduce.getInstance();
		List<String> dataList = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			String json = hits[i].getSourceAsString();
			if (topic.equals(LoadValues.LABEL_QBAOLOG)) {
				// qbaoLog数据格式单独处理
				QbaoLog qbaoLog = JSON.parseObject(hits[i].getSourceAsString(), QbaoLog.class);
				if (null != qbaoLog) {
					// stamp + begin 时间转换
					if (StringUtils.isNotEmpty(qbaoLog.getStamp())) {
						try {
							Long stampLg = Long.parseLong(qbaoLog.getStamp());
							qbaoLog.setStamp(DateUtil.LongToString(stampLg));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (StringUtils.isNotEmpty(qbaoLog.getBegin())) {
						try {
							Long benginLg = Long.parseLong(qbaoLog.getBegin());
							qbaoLog.setBegin(DateUtil.LongToString(benginLg));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					json = JSON.toJSONString(qbaoLog);
				}
			}
			dataList.add(json);
		}
		producer.sendListData(LoadValues.KAFKA_TOPICS_PRE + topic, dataList);
		logger.info("kafka发送成功： topic = {}, count={}", topic, hits.length);
		return true;
	}

	/**
	 * recordTask input
	 * 
	 * @throws Exception
	 */
	public String addRecordTask(String type, Long totalNum, Date inputTime) throws Exception {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		try {
			param.put("type", type);
			param.put("total_num", totalNum);
			param.put("operate_time", DateUtil.formatDate(inputTime, DateUtil.DATATIMEF_STR));

			DataInputEsService.getInstance();
			BulkRequestBuilder bulkRequest = DataInputEsService.client.prepareBulk();
			bulkRequest.add(
					client.prepareIndex(LoadValues.LABEL_TASKRECORD, LoadValues.LABEL_TASKRECORD).setSource(param));

			BulkResponse a = bulkRequest.execute().actionGet();
			logger.info("===es input recorder :" + a.buildFailureMessage() + " code= " + a.hasFailures());

			if (a.hasFailures()) {
				return "false";
			} else {
				return "success";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return "false";
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO kafka数据入库流程
		/**
		 * 注意点：BA项目单机启动，不能多机 1.ES 定时任务操作日志表里拉取最近一次数据，获取上次操作的最后时间。
		 * 2.根据以上时间，加当前时间。拉取对应的数据日志。
		 * 3.拉取时间段数据，判断总条数，设置默认5000条拉取一次。如果超过最大值，根据总条数计算分页，循环获取 4.拉取推送完毕，更新ES
		 * 操作记录
		 * 
		 * 问题： 1。数据可能会多次推送。接收方根据uuid去重
		 * 2.实时性问题，如果间隔时间很短的推送，有可能上次操作还没结束，重复操作相同数据，大量积压导致阻塞
		 * （考虑前期数据量大，间隔时间拉长。可以考虑当前处理完成后，设置下次定时任务时间，如果执行过程系统奔溃，会导致定时任务停止执行，
		 * 可以设置定时任务告警机制，如果数据1天没有更新，报警人工干预）
		 * 
		 * 
		 * 
		 */
		System.out.println(System.currentTimeMillis());
//		DataInputEsService service = DataInputEsService.getInstance();
//		 service.delete("taskrecord", "taskrecord");
//		System.out.println(service.dealTouTiaoDataInput());
//		System.out.println(service.dealQbaoLogDataInput());
//		System.out.println(service.dealLbsDataInput());

		
		
//		System.out.println(System.currentTimeMillis());

	}

}
