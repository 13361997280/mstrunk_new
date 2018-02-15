package data.service;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import po.PoiHttpPo;
import util.DateUtil;
import util.JsonResult;

public class PoiEsDataService {

	private static ESLogger logger = Loggers.getLogger(PoiEsDataService.class);
	private static PoiEsDataService poiEsDataService;
	private static TransportClient client;

	public static final PoiEsDataService getInstance() {
		try {
			if (poiEsDataService == null) {
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
					poiEsDataService = new PoiEsDataService();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return poiEsDataService;
	}

	/**
	 * poiInput
	 * 
	 * @throws Exception
	 */
	public static String add(PoiHttpPo po) throws Exception {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		try {
			param.put("poi_id", po.getPoiId());
			param.put("x_b", po.getxB());
			param.put("y_b", po.getyB());
			param.put("x_g", po.getxG());
			param.put("y_g", po.getyG());
			param.put("x_b_1", po.cutThreeNumber(po.getxB()));
			param.put("y_b_1", po.cutThreeNumber(po.getyB()));
			param.put("x_g_1", po.cutThreeNumber(po.getxG()));
			param.put("y_g_1", po.cutThreeNumber(po.getyG()));
			param.put("shen_code", po.getShenCode());
			param.put("shen", po.getShen());
			param.put("shi_code", po.getShiCode());
			param.put("shi", po.getShi());
			param.put("qu_code", po.getQuCode());
			param.put("qu", po.getQu());
			param.put("poi_name", po.getPoiName());
			param.put("poi_name_fenci", po.getPoiName());
			param.put("road_name", po.getRoadName());
			param.put("road_name_fenci", po.getRoadName());
			param.put("poi_type", po.getPoiType());
			param.put("poi_type_tag", po.getPoiTypeTag());
			param.put("update", DateUtil.formatDate(new Date(), DateUtil.DATATIMEF_STR));

			
			PoiEsDataService.getInstance();
			BulkRequestBuilder bulkRequest = PoiEsDataService.client.prepareBulk();
			bulkRequest.add(client.prepareIndex(LoadValues.LBS_INDEX, LoadValues.LBS_INDEX)
					.setId(param.get("poi_id").toString()).setSource(param));

			BulkResponse a = bulkRequest.execute().actionGet();
			logger.info("===es input lbs :" + a.buildFailureMessage() + " code= " + a.hasFailures());

			if (a.hasFailures()) {
				return JsonResult.failed(404, a.buildFailureMessage()).getJsonStr();

			} else {
				return JsonResult.success().getJsonStr();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResult.failed(e.toString()).getJsonStr();
		}
	}
	
	/**
	 * poiInput
	 * 
	 * @throws Exception
	 */
	public static String addList(List<PoiHttpPo> poList) throws Exception {
		logger.info("===es input lbs list begin:" + poList.size());
		try {
			Map<String, Object> param = new LinkedHashMap<String, Object>();
			// 构造es批量数据，批量一次性插入
			PoiEsDataService.getInstance();

			BulkRequestBuilder bulkRequest = PoiEsDataService.client.prepareBulk();
			for (PoiHttpPo po : poList) {
				param.put("poi_id", po.getPoiId());
				param.put("x_b", po.getxB());
				param.put("y_b", po.getyB());
				param.put("x_g", po.getxG());
				param.put("y_g", po.getyG());
				param.put("x_b_1", po.cutThreeNumber(po.getxB()));
				param.put("y_b_1", po.cutThreeNumber(po.getyB()));
				param.put("x_g_1", po.cutThreeNumber(po.getxG()));
				param.put("y_g_1", po.cutThreeNumber(po.getyG()));
				param.put("shen_code", po.getShenCode());
				param.put("shen", po.getShen());
				param.put("shi_code", po.getShiCode());
				param.put("shi", po.getShi());
				param.put("qu_code", po.getQuCode());
				param.put("qu", po.getQu());
				param.put("poi_name", po.getPoiName());
				param.put("poi_name_fenci", po.getPoiName());
				param.put("road_name", po.getRoadName());
				param.put("road_name_fenci", po.getRoadName());
				param.put("poi_type", po.getPoiType());
				param.put("poi_type_tag", po.getPoiTypeTag());
				param.put("update", DateUtil.formatDate(new Date(), DateUtil.DATATIMEF_STR));
				// ID设置 x_b+y_b 的分别保留6位小数
				String id = po.cutSixNumber(po.getxB()) +";" + po.cutSixNumber(po.getyB());
				bulkRequest.add(client.prepareIndex(LoadValues.LBS_INDEX, LoadValues.LBS_INDEX)
						.setId(id).setSource(param));
			}
			
			BulkResponse a = bulkRequest.execute().actionGet();
			logger.info("===es input lbs list end:" + poList.size());
			if (a.hasFailures()) {
				return JsonResult.failed(404, a.buildFailureMessage()).getJsonStr();
			} else {
				return JsonResult.success().getJsonStr();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return JsonResult.failed(e.toString()).getJsonStr();
		}
	}
	
}
