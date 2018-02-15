package com.qbao.search.engine;

import java.io.UnsupportedEncodingException;

import com.qbao.search.engine.service.CommandService;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.Server;
import com.qbao.search.rpc.netty.SimpleHttpRequestHandler;

import vo.CommandPo;

public class CmdHandler extends SimpleHttpRequestHandler<Object> {
	private static final ESLogger logger = Loggers.getLogger(CmdHandler.class);

	public CommandPo getParms() throws UnsupportedEncodingException {

		CommandPo commandPo = new CommandPo(httpRequest);

		return commandPo;

	}

	@Override
	public void setServer(Server server) {
	}

	@Override
	protected Object doRun() throws Exception {
		String htmlFrame = "null";
		CommandPo commandPo = getParms();
		CommandService commandService = new CommandService();
		try {
			switch (commandPo.getType()) {
			case "indexupdate":
				if ("stop".equals(commandPo.getAction())) {
					
					htmlFrame=commandService.IndexStop(commandPo);
					break;
				}
				if ("filebackup".equals(commandPo.getAction())) {
					
					htmlFrame=commandService.IndexUpdate(commandPo,true);
					break;
				}
				htmlFrame=commandService.IndexUpdate(commandPo,false);
				break;

			default:
				htmlFrame = "unknow command,check again!";
				break;
			}
			return htmlFrame;
		} catch (Exception e) {
			logger.error("+++CmdHandler error!+++", e);
		}

		return htmlFrame;
	}

}
