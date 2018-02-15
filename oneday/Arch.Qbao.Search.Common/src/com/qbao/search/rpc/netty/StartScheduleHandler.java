package com.qbao.search.rpc.netty;

import com.qbao.search.rpc.ScheduleServer;
import com.qbao.search.rpc.Server;
import com.qbao.search.util.CommonUtil;

/**
 * @Description
 * 
 * @Copyright Copyright (c)2011
 * 
 * @Company ctrip.com
 * 
 * @Author li_yao
 * 
 * @Version 1.0
 * 
 * @Create-at 2011-12-6 15:02:24
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-6 15:02:24  	li_yao		1.0			Newly created
 */
public class StartScheduleHandler extends SimpleHttpRequestHandler<String> {

	ScheduleServer<String, String> server;

	@Override
	protected String doRun() throws Exception {
		return server.startSchedule(CommonUtil.getParam(httpRequest, "info"));
	}


	@SuppressWarnings("unchecked")
	@Override
	public void setServer(Server server) {
		this.server = (ScheduleServer<String, String>) server;
	}

}
