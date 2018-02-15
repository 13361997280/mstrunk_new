package com.qbao.search.conf;

import java.io.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

//import java.io.*;

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
 * @Create-at 2011-8-5 09:57:27
 * 
 * @Modification-history <br>
 *                       Date Author Version Description <br>
 *                       ------------------------------------------------------
 *                       ---- <br>
 *                       2011-8-5 09:57:27 li_yao 1.0 Newly created
 */
public class Config {

	final Map<String, String> kvPairs = new ConcurrentHashMap<String, String>();

	final File confFile;

	Config(String confPath) {
		confFile = new File(confPath);
	}

	public void set(String key, String value) {
		kvPairs.put(key, value);
	}

	public Properties copy() {
		Properties p = new Properties();
		for (Entry<String, String> entry : kvPairs.entrySet()) {
			p.put(entry.getKey(), entry.getValue());
		}
		return p;
	}

	void load() throws FileNotFoundException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(confFile);
			Properties properties = new Properties();
			properties.load(is);

			for (Entry<Object, Object> entry : properties.entrySet()) {
				kvPairs.put((String) entry.getKey(), (String) entry.getValue());
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	public String get(String key, String defaultValue) {
		String value = kvPairs.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value.trim();
	}

	public int getInt(String key, int defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public long getLong(String key, long defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public float getFloat(String key, float defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		String value = get(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public String get(String key) {
		String value = kvPairs.get(key);
		if (value == null) {
			return null;
		} else {

			try {
				return new String(value.getBytes("ISO-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void releaseSource() throws IOException {
		// sub class should do something
	}

	private static Config BASE_CONFIG;

	public static final Config getBase() {
		try {
			if (BASE_CONFIG == null) {
				synchronized (Config.class) {
					if (BASE_CONFIG == null) {

						// online
						BASE_CONFIG = new Config(System.getProperty("user.dir")
								+ "/conf/qbao/costin.properties");
						// Test
						// BASE_CONFIG = new
						// Config("D:\\workspace\\git\\searchengine\\conf\\lexis\\loadConfig.dev.properties");
						BASE_CONFIG.load();
					}
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return BASE_CONFIG;
	}

	private static Config PROPERTIES_CONFIG;
	public static final Config getMainProperties() throws FileNotFoundException, IOException {
		if (PROPERTIES_CONFIG == null) {
			synchronized (Config.class) {
				if (PROPERTIES_CONFIG == null) {
					PROPERTIES_CONFIG = new Config(System.getProperty("user.dir")
							+ "/conf/qbao/main.properties");
					PROPERTIES_CONFIG.load();
				}
			}
		}
		return PROPERTIES_CONFIG;
	}

	private static Config SUB_CONFIG;
	public static final Config getMainPropertiesByParent() throws FileNotFoundException, IOException {
		if (SUB_CONFIG == null) {
			synchronized (Config.class) {
				if (SUB_CONFIG == null) {
					SUB_CONFIG = new Config(System.getProperty("user.dir")
							+ "/conf/qbao/subordinate.properties");
					SUB_CONFIG.load();
				}
			}
		}
		return SUB_CONFIG;
	}

	private static Config CONFIG;

	public static final Config get() {
		if (CONFIG == null) {
			synchronized (DefaultConfig.class) {
				if (CONFIG == null) {
					CONFIG = new DefaultConfig();
				}
			}
		}
		return CONFIG;
	}

	private static class DefaultConfig extends Config {

		long lastModified = -1;

		Timer timer = new Timer();

		DefaultConfig() {
			
			super(System.getProperty("user.dir") + "/conf/qbao/costin.properties");

			try {
				// LoadConfig.get().load(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

			load();

			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					load();
				}

			}, 1000, 10 * 1000);

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					timer.cancel();
				}
			});
		}

		@Override
		void load() {
			try {
				long newLastModified = confFile.lastModified();
				if (newLastModified > 0 && newLastModified != lastModified) {
					super.load();
					lastModified = newLastModified;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void releaseSource() throws IOException {
			timer.cancel();
		}

	}

}
