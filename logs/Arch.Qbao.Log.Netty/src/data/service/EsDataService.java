package data.service;

import com.alibaba.fastjson.JSON;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import data.db.IndexConstant;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import util.DateUtil;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

public class EsDataService {

    private static ESLogger logger = Loggers.getLogger(EsDataService.class);
    private static EsDataService recommendDataService;
    private static TransportClient client;
    private static Connection conn;
    private static SimpleDateFormat df;
    private static SimpleDateFormat dateFormat;

    public static final EsDataService getInstance() {
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
                    recommendDataService = new EsDataService();
                    df = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return recommendDataService;
    }

    /**
     * 钱宝日志存放
     *
     * @param paramMapList 清理过后的日志数据
     */
//	public void saveQbaoLog(Map paramMap) {
    public void saveQbaoLog(List<Object> paramMapList) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (Object object : paramMapList) {
            Map paramMap = JSON.parseObject(object.toString(), Map.class);
            String logId = UUID.randomUUID().toString();
            paramMap.put("logId", logId);

            paramMap.forEach((k, v) -> {
                if (!(v instanceof String))
                    v = JSON.toJSONString(v);
                paramMap.put(k, v);
            });
            bulkRequest.add(client.prepareIndex(LoadValues.QBAO_LOG_INDEX, LoadValues.QBAO_LOG_INDEX).setId(logId).setSource
                    (paramMap));
        }

        BulkResponse responses = bulkRequest.execute().actionGet();
        if (responses.hasFailures()) {
            int eRow = 0;
            BulkItemResponse[] items = responses.getItems();
            for (BulkItemResponse item : items) {
                if (item.isFailed()) {
                    eRow++;
                    logger.error("log saveQbaoLog error message = {}", item.getFailureMessage());
                }
            }
            logger.info("log saveQbaoLog size = {} error save = {}", paramMapList.size(), eRow);
        }
        logger.info("EsDataService.saveQbaoLog");
    }

    public Map searchQbaoLog(String pageId) {

        Map dataMap = new HashMap();
        Map detailMap = new HashMap();

        String[] x = new String[7];
        Long[] y = new Long[7];

//		当前时间
        Date currentDate = DateUtil.getTodayDate();

//		前一天
        Date oneday = DateUtil.getDateAfterDays(currentDate, -1);
        oneday = DateUtil.calculateDate(oneday,23,59,59);

//		前七天
        Date sevenDay = DateUtil.getDateAfterDays(currentDate, -7);

//		查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("companyId", "101"))
                .must(QueryBuilders.termQuery("productId", "10113"))
                .must(QueryBuilders.termQuery("pageId", pageId))
                .must(QueryBuilders.rangeQuery("stampDate")
                        .gt(DateUtil.getDateTime(dateFormat.toPattern(), sevenDay))
                        .lt(DateUtil.getDateTime(dateFormat.toPattern(), oneday))
                );

//		聚合条件
        ExtendedBounds extendedBounds = new ExtendedBounds(df.format(sevenDay), df.format(oneday));

        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram("pv")
                .field("stampDate")
                .format("yyyy-MM-dd")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .extendedBounds(extendedBounds);


        SearchRequestBuilder search = client.prepareSearch().
                setIndices(IndexConstant.INDEX_QBAO_LOG)
                .setTypes(IndexConstant.INDEX_QBAO_LOG)
                .setSize(0)
                .setQuery(boolQueryBuilder)
                .addAggregation(aggregationBuilder)
                .setSize(0);
        SearchResponse response = search.execute().actionGet();

        Aggregations aggregations = response.getAggregations();
        InternalDateHistogram dateHistogram = aggregations.get("pv");

        List<InternalDateHistogram.Bucket> list = dateHistogram.getBuckets();

        for (int i = 0; i < list.size(); i++) {
            InternalDateHistogram.Bucket    buket = list.get(i);
            x[i] = buket.getKeyAsString();
            y[i] = buket.getDocCount();
        }

        detailMap.put("x",x);

        detailMap.put("y",y);

        long total = response.getHits().totalHits;

        dataMap.put("detail",detailMap);
        dataMap.put("total",total);

        return dataMap;

    }
}
