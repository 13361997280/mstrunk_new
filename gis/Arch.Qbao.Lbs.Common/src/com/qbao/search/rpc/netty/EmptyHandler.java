package com.qbao.search.rpc.netty;

import com.qbao.search.rpc.Server;

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
 * @Create-at 2011-12-19 15:32:44
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-19 15:32:44  	li_yao		1.0			Newly created
 */
public class EmptyHandler extends SimpleHttpRequestHandler<String> {


	@Override
	protected String doRun() throws Exception {
		return "";
	}

	@Override
	public void setServer(Server server) {
		// TODO Auto-generated method stub
	}

}
