package data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadConfig;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import po.NewsDetailPo;
import util.NewsCache;
import util.NewsCategoryCache;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class NewsDataService {

	private static ESLogger logger = Loggers.getLogger(NewsDataService.class);
	private static NewsDataService service;
	private static TransportClient client;
	private static SimpleDateFormat df;
	private static SimpleDateFormat date;

	public static final NewsDataService getInstance(){
		try {
			if (service == null) {
				synchronized (NewsDataService.class) {
					//-----------------es集群连接--------------------
					Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
							.put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证, 打开后集群名字不对也能连接上
							.build();
					client = new PreBuiltTransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(Config.get().get("es.addr"), Config.get().getInt("es.port", 9300))));

					service = new NewsDataService();
					df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					date = new SimpleDateFormat("yyyy-MM-dd");
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return service;
	}

	/**
	 * 保存newsids
	 * @throws Exception
	 */
	public void saveNewsIds(Map<String,Object> param) throws Exception {
		try {
			Integer thumb = param.get("thumb")==null?0:Integer.parseInt(param.get("thumb").toString());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("user_id",param.get("userId"));
			map.put("news_id",param.get("newsIds"));
			map.put("status",thumb);
			map.put("update_time",df.format(new Date()));
			saveThumb(map);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 保存点赞状态
	 * @throws Exception
	 */
	public void saveThumb(Map<String,Object> param) throws Exception {
		try {
			//开启批量插入
			String userId = param.get("user_id").toString();
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			bulkRequest.add(client.prepareIndex(LoadValues.THUMB_INDEX, LoadValues.THUMB_INDEX)
					.setId(userId+param.get("news_id").toString()).setSource(param));
			bulkRequest.execute();
			//清理新闻列表点赞状态缓存
			if(NewsCache.hasCache(userId)) {
				String con = (String) NewsCache.getInstance().get(userId);
				NewsCache.clear(con);
				NewsCache.clear(userId);
			}
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 通过userid查询新闻列表
	 */
	public JSONObject searchList(Map<String,Object> map) {
		long timer = new Date().getTime();
		String userId = (String)map.get("userId");
		Integer page = map.get("page")==null?1:Integer.parseInt(map.get("page").toString());
		Integer size = map.get("size")==null?5:Integer.parseInt(map.get("size").toString());
		String typeId = map.get("type")==null?"1":map.get("type").toString();
		String newsId = map.get("newsId")==null?"":map.get("newsId").toString();
		//缓存
		String cacheKey = "searchList."+userId+page+size+typeId+newsId;
		JSONObject entity = (JSONObject) NewsCache.getInstance().get(cacheKey);
		if(entity != null){
			entity.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
			return entity;
		}

		String typeStr = LoadConfig.newsCategroy.get(typeId);
		JSONObject returnJson = new JSONObject();
		returnJson.put("page",page);
		returnJson.put("size",size);
		returnJson.put("type",typeId);
		returnJson.put("typeName",typeStr);
		returnJson.put("updateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
		JSONArray newsArray = new JSONArray();
		Long total = 0l;
		Map<String,Object> itemsAndTotal = null;
		try {
			//新闻类型条件组装
			String constStr = typeStr;
			if (typeId.equals("1")) {
				itemsAndTotal = getUserAndNewsType(userId,size,page);
				newsArray = (JSONArray) itemsAndTotal.get("items");
				total = (Long) itemsAndTotal.get("total");
				constStr = "";
			}
			if(total==0){
				BoolQueryBuilder bool = QueryBuilders.boolQuery();
				if (constStr != null && !"".equals(constStr)) {
					BoolQueryBuilder newsTypeBool = QueryBuilders.boolQuery();
					String[] paramStrs = constStr.split(",");
					for (String para : paramStrs) {
						newsTypeBool = newsTypeBool.should(QueryBuilders.termQuery("news_type", para));
					}
					bool.must(newsTypeBool);
				}

				//新闻标签条件组装
				if (!"".equals(newsId)) {
					String tag = getNewsTag(newsId);
					String[] paramStrs = tag.split(",");
					BoolQueryBuilder newsTagsBool = QueryBuilders.boolQuery();
					for (String para : paramStrs) {
						newsTagsBool = newsTagsBool.should(QueryBuilders.termQuery("news_tags_fenci", para));
					}
					bool.must(newsTagsBool);
				}

				String[] fields = {"news_id", "image_url", "news_type", "title", "news_origin"};
				FetchSourceContext sourceContext = new FetchSourceContext(true, fields, null);
				SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
				searchSourceBuilder.fetchSource(sourceContext);
				searchSourceBuilder.postFilter(bool);

				SearchRequestBuilder requestBuilder = client.prepareSearch()
						.setIndices(LoadValues.SPIDER_INDEX)
						.setTypes(LoadValues.SPIDER_INDEX)
						.setSource(searchSourceBuilder)
						.setFrom((page - 1) * size)
						.setSize(size)
						.setQuery(bool).addSort("news_time", SortOrder.DESC).addSort("hot_time", SortOrder.DESC);
				SearchResponse response = requestBuilder.execute().actionGet();
				SearchHits hits = response.getHits();
				total = hits.getTotalHits();
				for (SearchHit hit : hits.getHits()) {
					JSONObject object = JSON.parseObject(hit.getSourceAsString());
					JSONObject object1 = new JSONObject();
					object1.put("newsId", object.getString("news_id"));
					object1.put("imageUrl", object.getString("image_url"));
					object1.put("newsType", object.getString("news_type"));
					object1.put("title", object.getString("title"));
					object1.put("newsOrigin", object.getString("news_origin"));
					object1.put("thumb", getNewsStatus(userId, object.getString("news_id")));
					newsArray.add(object1);
				}
				if("".equals(newsId)) {
					JSONObject list = LoadConfig.adv.get("list:" + page);
					if (list != null) {
						newsArray.add(list);
					}
				}
			}
			returnJson.put("items",newsArray);
			returnJson.put("total",total);
			NewsCache.getInstance().put(cacheKey,returnJson);
			NewsCache.getInstance().put(userId,cacheKey);
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询新闻列表所花时间:"+time);
		}
		return returnJson;
	}

	/**
	 * 获取点赞状态
	 */
	private Integer getNewsStatus(String userId,String newsId) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("_id",userId+newsId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.THUMB_INDEX)
				.setTypes(LoadValues.THUMB_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()==0)return 0;
			SearchHit hit = response.getHits().getHits()[0];
			JSONObject object = JSON.parseObject(hit.getSourceAsString());

			return Integer.parseInt(object.getString("status"));
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询新闻详情
	 */
	public NewsDetailPo searchNewsDetail(String newsId,String userId,String pageStr) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("_id",newsId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SPIDER_INDEX)
				.setTypes(LoadValues.SPIDER_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		NewsDetailPo newsPo= new NewsDetailPo();
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()>0) {
				SearchHit hit = response.getHits().getHits()[0];
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				newsPo.setNewsId(newsId);
				newsPo.setImageUrl(object.getString("image_url"));
				newsPo.setNewsType(object.getString("news_type"));
				newsPo.setTitle(object.getString("title"));
				newsPo.setNewsOrigin(object.getString("news_origin"));
				newsPo.setContent(object.getString("content"));
				newsPo.setNewsTags(object.getString("news_tags"));
				newsPo.setNewsTime(object.getString("news_time"));
				newsPo.setThumb(getNewsStatus(userId,newsId));
				Integer page = 1;
				if(!StringUtils.isEmpty(pageStr)) {
					page = Integer.parseInt(pageStr);
				}
				JSONObject list = LoadConfig.adv.get("detail:"+page);
				if(list!=null){
					newsPo.setAdvId(list.getString("advId"));
					newsPo.setAdvImg(list.getString("advImg"));
					newsPo.setAdvUrl(list.getString("advUrl"));
					newsPo.setAdvTime(list.getString("advTime"));
					newsPo.setAdvDesc(list.getString("advDesc"));
					newsPo.setAdvTitle(list.getString("advTitle"));
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return newsPo;
	}


	/**
	 * 获取用户自定义新闻类别列表
	 * @return
     */
	public JSONObject newsTypelist(String userId) {
		long timer = new Date().getTime();
		String cacheKey = "newstypelist."+userId;
		JSONObject entity = (JSONObject) NewsCategoryCache.getInstance().get(cacheKey);
		if(entity != null) {
			logger.info("get newstypelist :"+cacheKey);
			return entity;
		}
		JSONObject set = null;
		JSONObject returnJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			String type = getUserNewsTypeForSelect(userId);
			String categoryStr = type;
			if("".equals(type)||null==type||"null".equals(type)){//选中的
				categoryStr = LoadConfig.defaultNewsType.get("codes");
			}
			String[] selectArray = categoryStr.split(",");
			for(String select:selectArray){
				set = new JSONObject();
				set.put("key",select);
				set.put("value",LoadConfig.newsCategroy.get(select));
				jsonArray.add(set);
			}
			returnJson.put("select",jsonArray);

			jsonArray = new JSONArray();
			String unSelect = getUnselectNewsType(categoryStr);
			if("".equals(unSelect)){//未选中的
				returnJson.put("unselect",new JSONArray());
			}else {
				String[] unselectArray = unSelect.split(",");
				for(String unselect:unselectArray){
					set = new JSONObject();
					set.put("key",unselect);
					set.put("value",LoadConfig.newsCategroy.get(unselect));
					jsonArray.add(set);
				}
				returnJson.put("unselect",jsonArray);
			}
			NewsCategoryCache.getInstance().put(cacheKey,returnJson);
			return returnJson;
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询用户自定义新闻类别列表所花时间:"+time);
		}
		return null;
	}
	/**
	 * 获取用户新闻类别列表
	 * @return
	 */
	public JSONObject categorylist(String userId) {
		long timer = new Date().getTime();
		String cacheKey = "useridcategory."+userId;
		JSONObject entity = (JSONObject) NewsCategoryCache.getInstance().get(cacheKey);
		if(entity != null) {
			logger.info("get useridcategory :"+cacheKey);
			return entity;
		}
		JSONObject set = null;
		JSONObject returnJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			String type = getUserNewsTypeForSelect(userId);
			String categoryStr = type;
			if("".equals(type)||null==type){//选中的
				categoryStr = LoadConfig.defaultNewsType.get("codes");
			}
			String[] selectArray = categoryStr.split(",");
			for(String select:selectArray){
				set = new JSONObject();
				set.put("key",select);
				set.put("value",LoadConfig.newsCategroy.get(select));
				jsonArray.add(set);
			}
			returnJson.put("items",jsonArray);
			NewsCategoryCache.getInstance().put(cacheKey,returnJson);
			return returnJson;
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			String time = (new Date().getTime() - timer) + " ms";
			logger.info("查询新闻类别列表所花时间:"+time);
		}
		return null;
	}

	/**
	 * 保存用户新闻类型
	 * @throws Exception
	 */
	public void saveUserNewsType(Map<String,Object> map) throws Exception {
		try {
			 Map<String,Object> param = new HashMap<String,Object>();
			String userId = (String)map.get("userId");
			String select = (String)map.get("select");
			param.put("user_id",userId);
			param.put("select",select);
			param.put("update_time",df.format(new Date()));
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			bulkRequest.add(client.prepareIndex(LoadValues.NEWSTYPE_INDEX, LoadValues.NEWSTYPE_INDEX)
					.setId(userId).setSource(param));
			bulkRequest.execute();
			//清理新闻列表点赞状态缓存
			String newstypelistKey = "newstypelist."+userId;
			String useridcategoryKey = "useridcategory."+userId;
			NewsCategoryCache.clear(newstypelistKey);
			NewsCategoryCache.clear(useridcategoryKey);
			logger.info("clearcache useridcategory and newstypelist:"+newstypelistKey+"|"+useridcategoryKey);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取用户新闻类型
	 */
	private String getUserNewsTypeForSelect(String userId) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("_id",userId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.NEWSTYPE_INDEX)
				.setTypes(LoadValues.NEWSTYPE_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		String returnStr = "";
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()==0)return returnStr;
			SearchHit hit = response.getHits().getHits()[0];
			JSONObject object = JSON.parseObject(hit.getSourceAsString());
			returnStr = object.getString("select");
			return returnStr;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	private String getUnselectNewsType(String select){
		String unSelectNewsType = "";
		if(select==null) return "";
		select = ","+select + ",";
		String defaultNewsTypeCode = LoadConfig.defaultNewsType.get("codes");
		String[] defaultNewsTypeCodeArray = defaultNewsTypeCode.split(",");
		for(int i=0;i<defaultNewsTypeCodeArray.length;i++){
			String matchStr = ","+defaultNewsTypeCodeArray[i]+",";
			if(!select.contains(matchStr)){
				unSelectNewsType = unSelectNewsType + defaultNewsTypeCodeArray[i] + ",";
			}
		}
		if(!"".equals(unSelectNewsType)){
			unSelectNewsType = unSelectNewsType.substring(0,unSelectNewsType.length()-1);
		}
		return unSelectNewsType;
	}


	/**
	 * 查询新闻详情中的tag
	 */
	public String getNewsTag(String newsId) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("_id",newsId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SPIDER_INDEX)
				.setTypes(LoadValues.SPIDER_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		String returnStr = "";
		try {
			response = requestBuilder.execute().get();
			if(response.getHits().getTotalHits()>0) {
				SearchHit hit = response.getHits().getHits()[0];
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				returnStr = object.getString("news_tags");
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return returnStr;
	}
	public Map<String,Object> getUserAndNewsType(String userId,Integer size,Integer page) {
		Map<String,Object> itemsAndTotal = new HashMap<String,Object>();
		JSONArray newsArray = new JSONArray();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id",userId));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SF_NEWSTYPE_INDEX)
				.setTypes(LoadValues.SF_NEWSTYPE_INDEX)
				.setSize(size)
				.setQuery(boolQueryBuilder).addSort("score", SortOrder.DESC);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			SearchHits hits = response.getHits();
			String typeReStr = "";
			if(hits.getTotalHits() > 0) {
				for (SearchHit hit : hits.getHits()) {
					JSONObject object = JSON.parseObject(hit.getSourceAsString());
					typeReStr = typeReStr + object.getString("news_type")+",";
				}
			}
			if(StringUtils.isEmpty(typeReStr)) {
				typeReStr = LoadConfig.defaultNewsType.get("names");
			}
			String newsType = typeReStr;
			String currentTime = date.format(new Date());
			String startTime = currentTime+" 00:00";
			String endTime = currentTime+" 23:59";
			boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.rangeQuery("news_time").gt(startTime).lt(endTime));
			String[] paramStrs = newsType.split(",");
			BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
			for (String para : paramStrs) {
				shouldBuilder = shouldBuilder.should(QueryBuilders.termQuery("news_type", para));
			}
			boolQueryBuilder.must(shouldBuilder);
			requestBuilder = client.prepareSearch()
					.setIndices(LoadValues.SPIDER_INDEX)
					.setTypes(LoadValues.SPIDER_INDEX)
					.setFrom((page - 1) * size)
					.setSize(size)
					.setQuery(boolQueryBuilder).addSort("news_time", SortOrder.DESC).addSort("hot_time", SortOrder.DESC);;
			response = requestBuilder.execute().get();
			hits = response.getHits();
			for (SearchHit hit : hits.getHits()) {
				JSONObject object = JSON.parseObject(hit.getSourceAsString());
				JSONObject object1 = new JSONObject();
				object1.put("newsId",object.getString("news_id"));
				object1.put("imageUrl",object.getString("image_url"));
				object1.put("newsType",object.getString("news_type"));
				object1.put("title",object.getString("title"));
				object1.put("newsOrigin",object.getString("news_origin"));
				object1.put("thumb",getNewsStatus(userId,object.getString("news_id")));
				newsArray.add(object1);
			}
			JSONObject list = LoadConfig.adv.get("list:" + page);
			if (list != null&&hits.getTotalHits()>0) {
				newsArray.add(list);
			}
			itemsAndTotal.put("items",newsArray);
			itemsAndTotal.put("total",hits.getTotalHits());
			return itemsAndTotal;

		}catch (Exception ex){
			ex.printStackTrace();
		}
		return itemsAndTotal;
	}

	/**
	 * 批量获取，通过scroll方式
	 *
	 */
	public String getBatchDataByScroll(String fromIndex, String fromType,String toIndex, String toType) {
		Long totalNum = 0L;
		try {
			SearchResponse response = client.prepareSearch()
					.setIndices(fromIndex)
					.setTypes(fromType)
					.setQuery(QueryBuilders.matchAllQuery())
					.setSize(10000)
					.setScroll(new TimeValue(600000))
					.setSearchType(SearchType.DEFAULT).execute().actionGet();

			String scrollid = response.getScrollId();
			totalNum = response.getHits().getTotalHits();
			while (true) {
				Thread.sleep(5000);
				// 发送数据到kafka
				inputData(response.getHits().getHits(), toIndex,toType);
				response = client.prepareSearchScroll(scrollid).setScroll(new TimeValue(1000000)).execute().actionGet();
				// 再次查询不到数据时跳出循环
				SearchHit[] hits = response.getHits().getHits();
				if (hits.length == 0) {
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "fromIndex=" + fromIndex + "->toIndex=" + toIndex + ",totalNum=" + totalNum;
	}

	public void inputData(SearchHit[] hits,String index,String type){
		for (int i = 0; i < hits.length; i++) {
			JSONObject object = JSON.parseObject(hits[i].getSourceAsString());

		}
	}

	public static void main(String[] args) throws Exception{

		String cons = "国际,社会,军事";
		//getInstance().saveUserIdsAndRecommend();
		LoadConfig.get();
		System.out.println(getInstance().getUnselectNewsType("12"));
		//getInstance().testBatchExport();
		//getInstance().delete(LoadValues.THUMB_INDEX,LoadValues.THUMmatchStrB_INDEX);
		//getInstance().delete(LoadValues.USERIDS_INDEX,LoadVnewsPosalues.USERIDS_INDEX);
		//getInstance().delete(LoadValues.SPIDER_INDEX,LoadValues.SPIDER_INDEX);
		//getInstance().delete(LoadValues.LABEL_INDEX,"user_label");
		//getInstance().searchNewsList(cons);
		//System.out.println(getInstance().searchNewsDetail("a644129841733566489").toString());
	}


}
