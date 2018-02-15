package data.service;

import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import vo.TimeSort;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author song.j
 * @create 2017-09-15 11:11:48
 **/
public class CompassDataService {
    private static ESLogger logger = Loggers.getLogger(CompassDataService.class);
    private static CompassDataService recommendDataService;
    private static TransportClient client;

    private static final Map<Integer, String> paramMap = new HashMap() {{
        put(10101, "news");
        put(10102, "weather");
//        put( 10103 , "shopping" );
//        put(10104, "baoy");
//        put( 10105 , "sign" );
        put(10106, "research");
        put(10107, "express");
        put(10108, "rates");
//        put( 10109 , "goodThings" );
    }};

    public static final CompassDataService getInstance() {
        try {
            if (recommendDataService == null) {
                synchronized (CompassDataService.class) {
                    //-----------------es集群连接--------------------
                    Settings settings = Settings.builder().put("cluster.name", Config.get().get("es.cluster.name")) // 设置集群名
                            .put("client.transport.ignore_cluster_name", true) // 忽略集群名字验证, 打开后集群名字不对也能连接上
                            .build();
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(Config.get().get("es.addr"), Config.get().getInt("es.port", 9300))));
                    recommendDataService = new CompassDataService();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return recommendDataService;
    }


    /**
     * 获取 es 数据
     *
     * @param userId
     * @return [SftimeAll{max_hour=0, module_id='sign'},
     * SftimeAll{max_hour=6, module_id='sign'},
     * SftimeAll{max_hour=15, module_id='rates'},
     * SftimeAll{max_hour=15, module_id='research'},
     * SftimeAll{max_hour=20, module_id='sign'}]
     */

    public List<TimeSort> getUserOneday(String userId) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("user_id", userId));

        logger.info("CompassDataService-getUserOneday search time es userId = {}", userId);

        SearchRequestBuilder requestBuilder = client.prepareSearch()
                .setIndices(LoadValues.SF_TIME_ALL).setTypes(LoadValues.SF_TIME_ALL)
                .setQuery(boolQueryBuilder)
                .addSort("stat_date", SortOrder.DESC)
                .addSort("score", SortOrder.DESC)
                .setFetchSource(new String[]{"max_hour", "module_id"}, new String[]{"id"});

        //查询用户的 时间 聚合数据
        SearchResponse response = requestBuilder.execute().actionGet();

        //用户时间及模块树集合
        List<TimeSort> data = new ArrayList();
        //做数据包装
        response.getHits().forEach(hits -> {

            Map hitMap = hits.getSourceAsMap();

            String module = paramMap.get(hitMap.get("module_id"));
            data.add(new TimeSort(hitMap.get("max_hour"), module));


        });

        return data;
    }
}
