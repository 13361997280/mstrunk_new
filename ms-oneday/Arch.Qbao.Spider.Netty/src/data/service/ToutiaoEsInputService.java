package data.service;

import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

public class ToutiaoEsInputService {

	private static ESLogger logger = Loggers.getLogger(ToutiaoEsInputService.class);
	private static ToutiaoEsInputService recommendDataService;
	private static TransportClient client;

	public static final ToutiaoEsInputService getInstance(){
		try {
			if (recommendDataService == null) {
				synchronized (ToutiaoEsInputService.class) {
					//-----------------es集群连接--------------------
					Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
							.put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证, 打开后集群名字不对也能连接上
							.build();
					client = new PreBuiltTransportClient(settings)
							.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(Config.get().get("es.addr"), Config.get().getInt("es.port", 9300))));
					//-----------------mysql数据库连接--------------------
					recommendDataService = new ToutiaoEsInputService();
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
	public void importEsFromSpider(Map<String,Object> param) throws Exception {
		try {
			//开启批量插入
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			bulkRequest.add(client.prepareIndex(LoadValues.SPIDER_INDEX, LoadValues.SPIDER_INDEX).setId(param.get("news_id").toString()).setSource(param));

			BulkResponse a = bulkRequest.execute().actionGet();
			if (a.hasFailures()) {
				System.out.println("===es input toutiao fail , new_id= "+ param.get("news_id") +".  msg:"+a.buildFailureMessage());
			}else {
				System.out.println("===es input toutiao ok new_id= "+ param.get("news_id"));
			}
				
			
			
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 是否存在记录
	 */
	public boolean isExist(String id,String title,String imageUrl) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.termQuery("_id",id));
		boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.termQuery("title",title));
		boolQueryBuilder = boolQueryBuilder.should(QueryBuilders.termQuery("image_url",imageUrl));
		SearchRequestBuilder requestBuilder = client.prepareSearch()
				.setIndices(LoadValues.SPIDER_INDEX)
				.setTypes(LoadValues.SPIDER_INDEX)
				.setQuery(boolQueryBuilder);
		SearchResponse response = null;
		try {
			response = requestBuilder.execute().get();
			Long hit = response.getHits().getTotalHits();
			if(hit.intValue()>0) return true;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception{
		System.out.println(getInstance().isExist("a6431038339420766465","这游戏男女搭配更好玩","http://p3.pstatp.com/list/190x124/26ed0004d39a394fb5e01"));

	}

}
