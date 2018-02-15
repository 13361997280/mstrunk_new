package com.qbao.search.engine;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import com.alibaba.fastjson.JSON;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;
import com.qbao.search.util.CommonUtil;

import data.service.PoiEsDataService;
import po.PoiHttpPo;
import util.JsonResult; 

public class PoiInputHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(PoiInputHandler.class);

	public PoiHttpPo getParms() throws UnsupportedEncodingException {

		PoiHttpPo poiHttpPo = new PoiHttpPo();
		poiHttpPo.setPoiId(CommonUtil.getParam(httpRequest, "poiId"));
		poiHttpPo.setxB(new BigDecimal(CommonUtil.getParam(httpRequest, "xB")));
		poiHttpPo.setyB(new BigDecimal(CommonUtil.getParam(httpRequest, "yB")));
		poiHttpPo.setxG(new BigDecimal(CommonUtil.getParam(httpRequest, "xG")));
		poiHttpPo.setyG(new BigDecimal(CommonUtil.getParam(httpRequest, "yG")));
		poiHttpPo.setShen(CommonUtil.getParam(httpRequest, "shen"));
		poiHttpPo.setShenCode(CommonUtil.getParam(httpRequest, "shenCode"));
		poiHttpPo.setShi(CommonUtil.getParam(httpRequest, "shi"));
		poiHttpPo.setShiCode(CommonUtil.getParam(httpRequest, "shiCode"));
		poiHttpPo.setQu(CommonUtil.getParam(httpRequest, "qu"));
		poiHttpPo.setQuCode(CommonUtil.getParam(httpRequest, "quCode"));
		poiHttpPo.setPoiName(CommonUtil.getParam(httpRequest, "poiName"));
		poiHttpPo.setRoadName(CommonUtil.getParam(httpRequest, "roadName"));
		poiHttpPo.setPoiType(CommonUtil.getParam(httpRequest, "poiType"));
		poiHttpPo.setPoiTypeTag(CommonUtil.getParam(httpRequest, "poiTypeTag"));

		logger.info("poi_input_url 参数:" + poiHttpPo.poiInputtoString());

		return poiHttpPo;

	}
	
	/**
	 * post 请求处理 获取参数值
	 * @return
	 * @throws Exception
	 */
	public List<PoiHttpPo> getListParms() throws Exception {

		HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(  
                new DefaultHttpDataFactory(false), httpRequest);  
        InterfaceHttpData postData = decoder.getBodyHttpData("poiList");
        List<PoiHttpPo> list = new ArrayList<PoiHttpPo>();                                                          
        // 读取从客户端传过来的参数  
        if (postData.getHttpDataType() == HttpDataType.Attribute) {  
            Attribute attribute = (Attribute) postData;  
            String json = attribute.getValue();
            try {
                list = JSON.parseArray(json, PoiHttpPo.class);
			} catch (Exception e) {
				return null;
			}
        }
		return list;

	}

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		String uri = httpRequest.getUri();
		if (uri.contains("api/v1.0/addLocationInfoList")) {
			// post请求批处理接口
			List<PoiHttpPo> poList = getListParms();
			if (null == poList || poList.size() <= 0) {
				// 失败
				return JsonResult.failed("input data null").getJsonStr();
			}
			return PoiEsDataService.addList(poList);
		} else {
			try {
				PoiHttpPo PoiHttpPo = getParms();
				if (!poiCheck(PoiHttpPo).equals("ok")) {
					// 校验失败
					return JsonResult.failed("parameter error : " + poiCheck(PoiHttpPo)).getJsonStr();
				}
				return PoiEsDataService.add(PoiHttpPo);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("+++PoiInputHandler error!+++", e);
				return JsonResult.failed().getJsonStr();
			}
		}
	}

	// 检查poi数据字段是否完整
	public String poiCheck(PoiHttpPo poi) {
		if (isStringEmpty(poi.getPoiId()))
			return "PoiId is null";
		if (isStringEmpty(poi.getxB().toString()))
			return "xB  is null";
		if (isStringEmpty(poi.getxG().toString()))
			return "xG is null";
		if (isStringEmpty(poi.getyB().toString()))
			return "yB is null";
		if (isStringEmpty(poi.getyG().toString()))
			return "yG is null";
		if (isStringEmpty(poi.getShen()))
			return "shen is null";
		if (isStringEmpty(poi.getShenCode()))
			return "shenCode is null";
		if (isStringEmpty(poi.getShi()))
			return "shi is null";
		if (isStringEmpty(poi.getShiCode()))
			return "shiCode is null";
		if (isStringEmpty(poi.getQu()))
			return "qu is null";
		if (isStringEmpty(poi.getQuCode()))
			return "quCode is null";
		if (isStringEmpty(poi.getPoiName()))
			return "poiName is null";

		return "ok";
	}

	public boolean isStringEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

}
