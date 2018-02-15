package com.qbao.search.rpc.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.jboss.netty.util.CharsetUtil;

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
 * @Create-at 2011-12-19 15:32:44
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-12-19 15:32:44  	li_yao		1.0			Newly created
 */
public class ChmodOtherReadableHandler extends SimpleHttpRequestHandler<String> {


	@Override
	protected String doRun() throws Exception {
		String plusMinus = CommonUtil.getBooleanParam(httpRequest,
				"readable") ? "+" : "-";
		String file = httpRequest.getContent().toString(CharsetUtil.UTF_8);
		String command = "chmod o" + plusMinus + "r " + file;
		Process process  = Runtime.getRuntime().exec(command);
		StringBuilder sb = new StringBuilder(1000);
		sb.append("input:\r\n");
		readStream(sb, process.getInputStream());
		sb.append("error:\r\n");
		readStream(sb, process.getErrorStream());
		sb.append("output:\r\n");
		sb.append(process.getOutputStream().toString());
		return sb.toString();
	}
	
	void readStream(StringBuilder sb, InputStream is) throws IOException {
	    BufferedReader br = new BufferedReader(
	    		new InputStreamReader(is, "UTF-8")); 
        String line = br.readLine(); 
        while((line = br.readLine()) != null) {
             sb.append(line).append("\r\n");
        }
	}

	@Override
	public void setServer(Server server) {
		// TODO Auto-generated method stub
	}

}
