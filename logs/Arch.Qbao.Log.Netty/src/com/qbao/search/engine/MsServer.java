package com.qbao.search.engine;

import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadConfig;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.engine.qbaolog.QbaoLogJob;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.netty.HttpRequestParser;
import com.qbao.search.rpc.netty.HttpServer;
import org.apache.zookeeper.KeeperException;
import util.Properties;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 用户画像启动容器
 * @author fanyunlong
 *
 */
public class MsServer extends HttpServer {

	private static ESLogger logger = Loggers.getLogger(MsServer.class);

	final protected MsParser LeixsParser;

	public MsServer(boolean shouldRecover) throws Throwable {
		super("LeixsServer", true);
		LeixsParser = new MsParser();

	}

	@Override
	public void stop() throws Exception {
		try {
			super.stop();
		} finally {
		}
	}

	/**
	 * @param args
	 * @throws Throwable
	 * @throws KeeperException
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static void main(String[] args) throws Throwable {
		try {
			Config.get().set("Root.path", new File(System.getProperty("user.dir")).getParentFile().getPath());
			
			LoadConfig.get();
			
			System.out.println("++++ proprertyFile.path = "+System.getProperty("user.dir") + "/conf/qbao/costin.properties");
			System.out.println("+++++ Root.path = " + new File(System.getProperty("user.dir")).getParentFile().getPath());
			System.out.println("+++++ Engine.user.dir = " + System.getProperty("user.dir"));
			System.out.println("+++++ Engine.user.name = " + System.getProperty("user.name"));
			System.out.println("+++++ Engine.user.home = " + System.getProperty("user.home"));
			System.out.println("+++++ Engine.port = " +Config.get().get("engine.port"));
			System.out.println("+++++ DB.url = " + Config.getBase().get(LoadValues.CONFIG_CONNECTION).trim());
			System.out.println("+++++ ES.url = " + Config.get().get("es.addr"));

			// 先加载所有字典到内存
			//initAll.initAllDic();

			// 加载定时任务
//			QuartzTaskManager.getInstance();

			//日志队理job
			new QbaoLogJob().start();

			new MsServer(args.length > 0 && args[0].equals("recover")).start(Properties.port);

			System.out.println("+++++engineer start ok!");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	protected HttpRequestParser getHttpRequestParser() {
		return LeixsParser;
	}

}