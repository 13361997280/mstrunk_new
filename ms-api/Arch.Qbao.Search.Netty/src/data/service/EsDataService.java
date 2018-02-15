package data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadConfig;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.util.HttpUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import util.DateUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class EsDataService {

	private static ESLogger logger = Loggers.getLogger(EsDataService.class);
	private static EsDataService recommendDataService;
	private static TransportClient client;
	private static Connection conn;
	private static SimpleDateFormat df;

	public static final EsDataService getInstance(){
		try {
			if (recommendDataService == null) {
				synchronized (EsDataService.class) {
					//-----------------es集群连接--------------------
					Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
							.put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证, 打开后集群名字不对也能连接上
							.build();
					client = new PreBuiltTransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(Config.get().get("es.addr"), Config.get().getInt("es.port", 9300))));
					//-----------------mysql数据库连接--------------------
					String driver = "com.mysql.jdbc.Driver";
					String user = Config.getBase().get(LoadValues.SEARCHDB_USERNAME).trim();
					String pwd = Config.getBase().get(LoadValues.SEARCHDB_PASSWORD).trim();
					String url = Config.getBase().get(LoadValues.CONFIG_CONNECTION);
					conn = DriverManager.getConnection(url, user, pwd);
					recommendDataService = new EsDataService();
					df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return recommendDataService;
	}

	/**
	 * 从文件系统批量导入数据测试程序
	 * @throws Exception
	 */
	public void testBatchImportFromFile() throws Exception {
		try {
			//读取刚才导出的ES数据
			BufferedReader br = new BufferedReader(new FileReader("userLabel2017-05-11"));
			String json = null;
			int count = 0;
			//开启批量插入
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			while ((json = br.readLine()) != null) {
				JSONObject jsonObject = JSONObject.parseObject(json);
				bulkRequest.add(client.prepareIndex("ms", "user_label").setId(jsonObject.getString("e_user_id")).setSource(json));
				//每一千条提交一次
				if (count% 1000==0) {
					bulkRequest.execute().actionGet();
					System.out.println("提交了：" + count);
				}
				count++;
			}
			bulkRequest.execute().actionGet();
			System.out.println("插入完毕");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 从批量导出数据测试程序
	 * @throws Exception
	 */
	public void testBatchExport() throws Exception {
		QueryBuilder qb2 = QueryBuilders.matchQuery("update_time", "2017-05-11");
		SearchRequestBuilder requestBuilder2 = client.prepareSearch().setQuery(qb2).setSize(2);
		MultiSearchResponse multiResponse = client.prepareMultiSearch().add(requestBuilder2)
				.execute().actionGet();
		BufferedWriter out = new BufferedWriter(new FileWriter("userLabel2017-05-11", true));
		for (MultiSearchResponse.Item item : multiResponse.getResponses()) {
			SearchResponse response1 = item.getResponse();
			SearchHit[] hits = response1.getHits().getHits();
			for (int i = 0; i < hits.length; i++) {
				String json = hits[i].getSourceAsString();
				out.write(json);
				out.write("\r\n");
				System.out.println(hits[i].getSourceAsString());
			}
			out.flush();
		}

	}

	/**
	 * 删除索引
	 */
	public void deleteIndex(String indexName) {
		try {
			DeleteIndexResponse response = client.admin().indices().prepareDelete(indexName).execute().get();
			logger.info("EsDataService.deleteIndex",response);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}


	}
	/**
	 * 删除es数据
	 */
	public void delete(String indexName,String type) {
		try {
			BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
							.filter(QueryBuilders.matchQuery("_type", type)).source(indexName).get();
			long deleted = response.getDeleted();
			logger.info("EsDataService.delete->count",deleted+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 标签查询主程序
	 */
	public Integer multySearch(Map paramMap) {
		BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(paramMap);
		//logger.info("EsDataService.multySearch->boolQueryBuilder:"+boolQueryBuilder.toString());
		SearchRequestBuilder requestBuilder = client.prepareSearch().setIndices(LoadValues.LABEL_INDEX).setTypes(LoadValues.LABEL_TYPE).setQuery(boolQueryBuilder);
		SearchResponse response=null;
		try {
			response = requestBuilder.execute().get();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		Long totalHits = response.getHits()==null?0l:response.getHits().getTotalHits();
		return  totalHits.intValue();
	}

	/**
	 * 拼装条件
	 * @param paramMap
	 * @return
     */
	private BoolQueryBuilder getBoolQueryBuilder(Map paramMap) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		int i = 0;
		if(paramMap!=null) {
			Iterator<Map.Entry<String, String>> iterator = paramMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String[] shouldArray = value.split(",");
				i++;
				if (shouldArray.length > 1) {
					for (int j = 0; j < shouldArray.length; j++) {
						if (StringUtils.isNotEmpty(shouldArray[j])) {
							boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.termQuery(key, shouldArray[j]));
						} else {
							boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.matchQuery(key, shouldArray[j]));
						}
					}
					continue;
				}
				String[] scopeArray = value.split("_");
				if (scopeArray.length > 1) {
					boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.rangeQuery(key).gt(scopeArray[0]).lt(scopeArray[1]).includeLower(true).includeUpper(false));
					continue;
				}
				boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery(key, value));
			}
		}
		if(i==0){
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchAllQuery());
		}
		return boolQueryBuilder;
	}


	/**
	 * 多条件查询es获取客群userid集合,逗号隔开
	 */
	public String getUserIds(Map paramMap) {
		Integer page = paramMap.get("page")==null?0:Integer.parseInt(paramMap.get("page").toString());
		Integer size = paramMap.get("size")==null?1000:Integer.parseInt(paramMap.get("size").toString());
		paramMap.remove("page");
		paramMap.remove("size");
		BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(paramMap);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		String[] fields = {"user_id"};
		FetchSourceContext sourceContext = new FetchSourceContext(true,fields,null);
        searchSourceBuilder.fetchSource(sourceContext);
		SearchRequestBuilder requestBuilder = client.prepareSearch().setIndices(LoadValues.LABEL_INDEX).
				setTypes(LoadValues.LABEL_TYPE).setSource(searchSourceBuilder).setQuery(boolQueryBuilder).setFrom(page*size).setSize(size);
		SearchResponse response = null;
		StringBuffer searchBuff = new StringBuffer();
		try {
			response = requestBuilder.get();
			if(response==null||response.getHits()==null) return null;
			for (SearchHit hit : response.getHits().getHits()) {
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				searchBuff.append(object.getString("user_id")+",");
			}
		}catch (Exception ex){
			logger.error("EsDataService.getUserIds",ex);
			return null;
		}
		String searchStr = searchBuff.toString();
		if(searchStr.equals(""))return searchStr;
		searchStr = searchStr.substring(0,searchStr.length()-1);
		return  searchStr;
	}
	/**
	 * 多条件查询es获取客群userid集合,逗号隔开
	 */
	public String getUserIds1(Map paramMap) {
		BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(paramMap);
		//logger.info("EsDataService.getUserIds->boolQueryBuilder:"+boolQueryBuilder.toString());
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(1000);
		String[] fields = {"e_user_id"};
		FetchSourceContext sourceContext = new FetchSourceContext(true,fields,null);
		searchSourceBuilder.fetchSource(sourceContext);
		SearchRequestBuilder requestBuilder = client.prepareSearch().setIndices(LoadValues.LABEL_INDEX).
				setTypes(LoadValues.LABEL_TYPE).setQuery(boolQueryBuilder).setSource(searchSourceBuilder);
		SearchResponse response=null;
		StringBuffer searchBuff = new StringBuffer();
		try {
			response = requestBuilder.execute().get();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		String searchStr = "";
		//SearchHit[] hits = response.getHits().getHits();
		//for (int i = 0; i < hits.length; i++) {
			//JSONObject object = JSON.parseObject(hits[i].getSourceAsString());
			//searchStr = searchStr + object.getString("e_user_id")+",";
		//}
		//searchStr = searchStr.substring(0,searchStr.length()-1);
		return  searchStr;
	}

	/**
	 * 多条件查询es获取客群userid集合,逗号隔开
	 */
	public JSONObject getJsonObjectStrForConst(String fieldName,String fieldValue,String indexName,String type,String fields) {
		JSONObject object=null;
		try {
			BoolQueryBuilder qb =  QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery(fieldName, fieldValue));
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes(type);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			if(fields!=null&&!"".equals(fields)){
				String[] fieldArray = fields.split(",");
				FetchSourceContext sourceContext = new FetchSourceContext(true,fieldArray,null);
				searchSourceBuilder.fetchSource(sourceContext);
			}
			searchSourceBuilder.postFilter(qb);
			searchRequestBuilder.setSource(searchSourceBuilder);
			SearchResponse response = searchRequestBuilder
					.execute().actionGet();
			SearchHit[] hits = response.getHits().getHits();
			if(hits.length==0) return null;
			object = JSON.parseObject(hits[0].getSourceAsString());
		}catch (Exception ex){
			ex.printStackTrace();
		}
		if(object==null)return null;
		return  object;
	}

	/**
	 * 对外查询
	 */
	public JSONObject multySearchForEntity(Map paramMap) {
		JSONObject returnJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Integer page = paramMap.get("page")==null?1:Integer.parseInt(paramMap.get("page").toString());
		Integer size = paramMap.get("size")==null?10:Integer.parseInt(paramMap.get("size").toString());
		paramMap.remove("page");
		paramMap.remove("size");
		String constParamStr = paramMap.get("const")==null?"":paramMap.get("const").toString();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		FetchSourceContext sourceContext = null;
		String[] fields = {"user_id"};
		if(!"".equals(constParamStr)){
			String[] constParamArray = constParamStr.split(",");
			if(constParamArray.length==1){
				sourceContext = new FetchSourceContext(true,new String[]{constParamStr},null);
			}else{
				sourceContext = new FetchSourceContext(true,constParamArray,null);
			}
		}else{
			sourceContext = new FetchSourceContext(true,fields,null);
		}
		paramMap.remove("const");
		BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(paramMap);
		//logger.info("EsDataService.multySearchForEntity->boolQueryBuilder:"+boolQueryBuilder.toString());
		searchSourceBuilder.fetchSource(sourceContext);
		searchSourceBuilder.postFilter(boolQueryBuilder);
		//logger.info("EsDataService.multySearchForEntity->searchSourceBuilder:"+searchSourceBuilder.toString());
		SearchRequestBuilder requestBuilder = client.prepareSearch().setIndices(LoadValues.LABEL_INDEX).
				setTypes(LoadValues.LABEL_TYPE).setSource(searchSourceBuilder)
				.setFrom(page-1)
				.setSize(size);
		SearchResponse response=null;
		try {
			response = requestBuilder.execute().get();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		for (SearchHit hit : response.getHits().getHits()) {
			JSONObject object = JSON.parseObject(hit.getSourceAsString());
			jsonArray.add(object);
		}
		returnJson.put("items",jsonArray);
		returnJson.put("total",response.getHits().getTotalHits());
		returnJson.put("page",page);
		returnJson.put("size",size);
		return returnJson;
	}
	/**
	 * 前端查询条件获取
	 */
	public JSONObject getSearchConstForFront(String fieldName,String matchStr) {
		QueryBuilder qb = QueryBuilders.termQuery(fieldName, matchStr);
		SearchResponse response = client.prepareSearch(LoadValues.GROUP_INDEX).setTypes(LoadValues.GROUP_INDEX).setQuery(qb)
					.execute().actionGet();
		SearchHit[] hits = response.getHits().getHits();
		JSONObject record = JSON.parseObject(hits[0].getSourceAsString());
		String result = record.getString("conditions");
		String[] resultArray = result.split("&");
		JSONObject list = new JSONObject();
		JSONArray listArray = new JSONArray();
		for(int i=0;i<resultArray.length;i++){
			JSONObject j2 = new JSONObject();
			String[] indexResult  = resultArray[i].split("=");
			String fieldNameP = indexResult[0];
			if(fieldNameP.equals("showList")||fieldNameP.equals("userId")
			||fieldNameP.equals("page")||fieldNameP.equals("size")) continue;
			String fieldValue = indexResult[1];
			JSONArray jsonArray = LoadConfig.dictOptionMap.get(fieldNameP);
			j2.put("name",LoadConfig.dictCodeName.get(fieldNameP));
			String names = "";
			if(jsonArray==null) {
				if("3".equals(LoadConfig.dictCodeType.get(fieldNameP))){
					names = LoadConfig.areas.get(fieldValue);
				}else if("4".equals(LoadConfig.dictCodeType.get(fieldNameP))){
					names = LoadConfig.citys.get(fieldValue);
				}else{
					continue;
				}
			}else {
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject j1 = jsonArray.getJSONObject(j);
					String name = j1.getString("name");
					String value = j1.getString("value");
					if (StringUtils.isNotEmpty(value)) {
						if (fieldValue.contains(",") && fieldValue.contains(value)) {
							names = names + name + ",";
						} else if (fieldValue.equals(value)) {
							names = names + name + ",";
						}
					}
				}
				names = names.substring(0, names.length() - 1);
			}
			j2.put("value",names);
			listArray.add(j2);
		}
		list.put("options",listArray);
		return JSON.parseObject(list.toJSONString());
	}

	/**
	 * 保存收藏的客群
	 * @param id
	 * @param collections
	 * @param userId
     */
	public void saveUserIds(String id,String collections,String userId) {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		ret.put("groupId",id);
		if(StringUtils.isEmpty(userId)) userId = "";
		ret.put("userId",userId);
		ret.put("collections",collections);
		ret.put("DateTime",DateUtil.getDateTime("yyyy-MM-dd",new Date()));
		bulkRequest.add(client.prepareIndex(LoadValues.USERIDS_INDEX, LoadValues.USERIDS_INDEX).setId(id).setSource(ret));
		bulkRequest.execute();
	}

	/**
	 * 保存查询条件及结果集
	 * @param condition
	 * @param result
	 * @param groupId
	 * @param userId
     * @return
     */
	public String saveConditionAndResult(String condition,String result,String groupId,String userId) {
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		ret.put("groupId",groupId);
		ret.put("userId",userId);
		ret.put("conditions",condition);
		ret.put("results",result);
		ret.put("DateTime", DateUtil.getDateTime("yyyy-MM-dd",new Date()));
		bulkRequest.add(client.prepareIndex(LoadValues.GROUP_INDEX, LoadValues.GROUP_INDEX).setId(groupId).setSource(ret));
		BulkResponse re = bulkRequest.execute().actionGet();
		return groupId;
	}

	/**
	 * 保存浏览日志
	 *
     */
	public void saveLog(Map paramMap) {
//		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		String logId = UUID.randomUUID().toString();
		paramMap.put("logId",logId);
//		ret.put("userId",userId);
//		ret.put("groupId",groupId);
//		ret.put("msgId",msgId);
//		ret.put("taskId",taskId);
//		ret.put("doTaskDateTime",doTaskDateTime);
//		ret.put("browsUserId",browsUserId);
//		ret.put("browseDateTime",browseDateTime);
//		ret.put("sendType",sendType);
////		ret.put("browseIp",browseIp);
		bulkRequest.add(client.prepareIndex(LoadValues.LOG_INDEX, LoadValues.LOG_INDEX).setId(logId).setSource(paramMap));
		BulkResponse a = bulkRequest.execute().actionGet();
		logger.info("EsDataService.saveLog",a.buildFailureMessage());
	}


	/**
	 * 标签查询主程序(聚合统计)
	 */
	public Map multySearchAggr(Map paramMap,String fieldName) {
		Map returnMap = new HashedMap();
		BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(paramMap);
		//logger.info("EsDataService.multySearch->boolQueryBuilder:"+boolQueryBuilder.toString());
		SearchRequestBuilder requestBuilder = client.prepareSearch().setIndices(LoadValues.LABEL_INDEX).setTypes(LoadValues.LABEL_TYPE)
				.setQuery(boolQueryBuilder).addAggregation(AggregationBuilders.terms("term").field(fieldName));
		SearchResponse response=null;
		try {
			response = requestBuilder.execute().get();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		StringTerms agg = response.getAggregations().get("term");
		for (Terms.Bucket entry : agg.getBuckets()) {
			String key = entry.getKeyAsString();            // bucket key
			long docCount = entry.getDocCount();            // Doc count
			returnMap.put(key,docCount+"");
		}
		return  returnMap;
	}

	public String getUserIds2(){
		String userIds = new String();
		String paramstr = "{\"size\":10000,\"_source\":\"e_user_id\"}";
		try{
			userIds = HttpUtils.postByJson("http://192.168.14.107:9200/userlabel/user_label/_search",paramstr);
			JSONArray jsonArray = JSON.parseArray("["+userIds+"]");
			System.out.println(jsonArray.size());
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return userIds;
	}

	public JSONObject getLog(Map paramMap){

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		int page = Integer.parseInt(paramMap.get("page").toString());
		int size = Integer.parseInt(paramMap.get("size").toString());

		paramMap.remove("page");
		paramMap.remove("size");
		BoolQueryBuilder boolQueryBuilder = getLogQuery(paramMap);
		//返回指定字段 全返回
//		searchSourceBuilder.fetchSource(sourceContext);
		searchSourceBuilder.postFilter(boolQueryBuilder);

		SearchRequestBuilder requestBuilder = client.prepareSearch().setIndices(LoadValues.LOG_INDEX).
				setTypes(LoadValues.LOG_INDEX).setSource(searchSourceBuilder)
				.setFrom((page - 1) * size)
				.setSize(size);
		SearchResponse response=null;


		try {
			response = requestBuilder.execute().get();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		List result = new ArrayList();
		for (SearchHit hit : response.getHits().getHits()) {
			result.add(hit.getSourceAsMap());
		}

		JSONObject returnJson = new JSONObject();
		returnJson.put("items",result);
		returnJson.put("total",response.getHits().getTotalHits());
		returnJson.put("page",page);
		returnJson.put("size",size);
		return returnJson;
	}


	private BoolQueryBuilder getLogQuery(Map paramMap) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (paramMap.get("userId") != null && StringUtils.isNotEmpty(paramMap.get("userId").toString())) {
			String value = paramMap.get("userId").toString();
			boolQueryBuilder.should(QueryBuilders.matchQuery("userId", value));
			boolQueryBuilder.should(QueryBuilders.matchQuery("msgId", value));
			boolQueryBuilder.should(QueryBuilders.matchQuery("groupId", value));
			boolQueryBuilder.should(QueryBuilders.matchQuery("browseIp", value));
			boolQueryBuilder.should(QueryBuilders.matchQuery("sendType", value));
			return boolQueryBuilder;
		}

		if (paramMap.get("startTime") != null && StringUtils.isNotEmpty(paramMap.get("startTime").toString())) {
			String value = paramMap.get("startTime").toString();
			value = DateUtil.dateStrTofmStr(value, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
			boolQueryBuilder.must(QueryBuilders.rangeQuery("browseDateTime").gt(value));

			value = paramMap.get("endTime").toString();
			value = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", DateUtil.dsDay_Date(DateUtil.StringtoDate(value,
					"yyyy-MM-dd"), 1));
			boolQueryBuilder.must(QueryBuilders.rangeQuery("browseDateTime").lt(value));

		}
		return boolQueryBuilder;
	}


	public static void main(String[] args) throws Exception{
		long timer = new Date().getTime();
		//System.out.println(new EsDataService().getKeyword("aa"));
		//getInstance().update2();
		//getInstance().testBatchImport("userlabel1","userlabel1");
		//getInstance().getJsonObjectStrForConst("id","19d34161-425b-4654-a034-06a2ec9aa95d","result","user_label");
		//getInstance().saveConditionAndResult("userlabel1","user_label");
		//getInstance().testBatchImportFromFile();
		//getInstance().testBatchExport();
		//getInstance().delete(LoadValues.GROUP_INDEX,LoadValues.GROUP_INDEX);
		//getInstance().delete(LoadValues.USERIDS_INDEX,LoadValues.USERIDS_INDEX);
		//getInstance().delete(LoadValues.LABEL_INDEX,"user_label");
		//getInstance().delete(LoadValues.RESULT_INDEX,LoadValues.RESULT_INDEX);
		//logger.info(getInstance().getUserIds(new String[]{"showList"},new String[]{"2017"}));
		//getInstance().deleteIndex("result");
		///logger.info(getInstance().search("e_user_id",2)+"");
		//logger.info(getInstance().multySearch(new String[]{"base_regit_date"},new String[]{"2017"})+"");
		///System.out.println(EsDataService.getInstance().getJsonObjectStrForConst("_id","78a22d9c-d0fa-4915-932e-e7c1ef7a9063",LoadValues.GROUP_INDEX,LoadValues.GROUP_INDEX,null));
		//getInstance().saveLog("11","11","11","11",
		//		"2017-05-27 10:10:10","11","2017-05-27 10:10:10",

		//System.out.println(getInstance().getUserIds2());
		//System.out.println("计算标签查询所花时间:"+(new Date().getTime() - timer) + " ms");
		//timer = new Date().getTime();
		//System.out.println(getInstance().getUserIds(null));
		//getInstance().saveLog("11","11","11","11",
		//		"2017-05-27 10:10:10","11","2017-05-27 10:10:10",
		//System.out.println("计算标签查询所花时间1:"+(new Date().getTime() - timer) + " ms");
		//		"1");



    }

}
