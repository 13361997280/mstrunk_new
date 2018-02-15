package com.qbao.search.engine;

import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadConfig;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;
import com.qbao.search.rpc.netty.HttpRequestParser;
import com.qbao.search.rpc.netty.HttpServer;
import data.service.RedisUnit;
import org.apache.zookeeper.KeeperException;
import util.Properties;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeoutException;

/**
 * 用户画像启动容器
 * @author fanyunlong
 *
 */
public class CreditServer extends HttpServer {

	private static ESLogger logger = Loggers.getLogger(CreditServer.class);

	final protected CreditParser LeixsParser;

	public CreditServer(boolean shouldRecover) throws Throwable {
		super("CreditServer", true);
		LeixsParser = new CreditParser();

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
			System.out.println("+++++ DB.url = " + Config.getBase().get(LoadValues.CREDIT_CONNECTION).trim());
			System.out.println("+++++ ES.url = " + Config.get().get("es.addr"));
            System.out.println("+++++ Thread size = " + Runtime.getRuntime().availableProcessors() * 2);

			// 先加载所有字典到内存
			//initAll.initAllDic();
			// 初始化
			RedisUnit.initAll();
			new CreditServer(args.length > 0 && args[0].equals("recover")).start(Properties.port);

            System.out.println("+++++ Credit Engineer start ok!");
            System.out.println("+++++ Start date " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

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