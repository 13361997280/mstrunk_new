package util;

import com.qbao.search.conf.Config;
import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import java.util.*;

/**
 * 本地新闻缓存管理
 * 
 * @author hz
 * 
 */
public class NewsCache {
	private static final ESLogger logger = Loggers.getLogger(NewsCache.class);
	private static Long LiveTime = Long.parseLong(Config.get().get("newscash.live.time", "3600000"));// 有效时间一小时
	private static Long internalTime = Long.parseLong(Config.get().get("newscash.clean.time", "3600000"));;// 每分钟定期清理时间
	private static TreeMap<String, CashUnit> cacheMap = new TreeMap<String, CashUnit>();
	private static NewsCache MYCASH;

	public static final NewsCache getInstance() {
		if (MYCASH == null) {
			synchronized (NewsCache.class) {
				if (MYCASH == null) {
					MYCASH = new NewsCache();
				}
			}
		}
		return MYCASH;
	}

	private NewsCache() {
		super();
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					synchronized (cacheMap) {
						int count = 0;
						logger.info("+++++ news Map定期清理 +++++ Size = " + cacheMap.size() + new Date().toString());
						Iterator iter = cacheMap.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							String key = (String) entry.getKey();
							CashUnit cache = getCacheInfo(key);
							if (cacheExpired(cache)) {
								++count;
								iter.remove();
								cacheMap.remove(key);
								logger.info("+++++ news Map定期清理 key = " + key);
							}
						}
						logger.info("+++++ news Map总数KEY = " + cacheMap.size() + ", count = " + count + new Date().toString());
					}
				} catch (Exception e) {
					logger.error(e);
				} catch (Throwable e) {
					logger.error(e);
				}
			}
		}, 30 * 1000, internalTime);
	}

	/**
	 * 获取缓存数据 如果超时则返回空
	 * 
	 * @param key
	 *            缓存key
	 * @return
	 */
	public Object get(String key) {
		CashUnit cache = getCacheInfo(key);
		return cache == null ? null : cache.isExpired() ? null : cache.getValue();
	}

	/**
	 * 判断是否存在一个缓存
	 * 
	 * @param key
	 *            缓存key
	 * @return
	 */
	public static boolean hasCache(String key) {
		synchronized (cacheMap) {
			return cacheMap.containsKey(key);
		}
	}

	/**
	 * 清除所有缓存
	 */
	public static void clearAll() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}

	/**
	 * 清除指定的缓存
	 * 
	 * @param key
	 *            缓存key
	 */
	public synchronized static void clear(String key) {
		synchronized (cacheMap) {
			cacheMap.remove(key);
		}
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void put(String key, Object obj) {
		putCacheInfo(key, obj, LiveTime);
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void put(String key, Object obj, long timeOut) {
		putCacheInfo(key, obj, timeOut);
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param key
	 *            缓存KEY
	 * @return 缓存数据
	 */
	public static CashUnit getCacheInfo(String key) {
		synchronized (cacheMap) {
			CashUnit cache = (CashUnit) cacheMap.get(key);
			if (cache == null) {
				return null;
			}
			if (cacheExpired(cache)) { // 调用判断是否终止方法
				cache.setExpired(true);
			}
			return cache;
		}
	}

	/**
	 * 把对象放入缓存中
	 * 
	 * @param key
	 *            缓存KEY
	 * @param obj
	 *            缓存内容
	 * @param dt
	 *            过期时间（毫秒）
	 */
	private static void putCacheInfo(String key, Object obj, long dt) {
		putCacheInfo(key, obj, dt, false);
	}

	/**
	 * 把数据放入缓存中
	 * 
	 * @param key
	 *            缓存KEY
	 * @param obj
	 *            缓存内容
	 * @param dt
	 *            过期时间（毫秒）
	 * @param expired
	 *            是否已过期
	 */
	private static void putCacheInfo(String key, Object obj, long dt, boolean expired) {
		CashUnit cache = new CashUnit();
		cache.setKey(key);
		cache.setTimeOut(dt + System.currentTimeMillis()); // 设置多久后更新缓存
		cache.setValue(obj);
		cache.setExpired(expired); // 缓存默认载入时，终止状态为FALSE
		synchronized (cacheMap) {
			cacheMap.put(key, cache);
		}
	}

	/**
	 * 判断缓存是否终止
	 * 
	 * @param cache
	 *            缓存
	 * @return
	 */
	public static boolean cacheExpired(CashUnit cache) {
		if (null == cache) { // 传入的缓存不存在
			return false;
		}
		long nowDt = System.currentTimeMillis(); // 系统当前的毫秒数
		long cacheDt = cache.getTimeOut(); // 缓存内的过期毫秒数
		if (cacheDt <= 0 || cacheDt > nowDt) { // 过期时间小于等于零时,或者过期时间大于当前时间时，则为FALSE
			return false;
		} else { // 大于过期时间 即过期
			return true;
		}
	}

	/**
	 * 获取缓存的大小
	 * 
	 * @return
	 */
	public static int getCacheSize() {
		synchronized (cacheMap) {
			return cacheMap.size();
		}
	}

	public static Long getLiveTime() {
		return LiveTime;
	}

	public static void setLiveTime(Long liveTime) {
		LiveTime = liveTime;
	}

	public static Long getInternalTime() {
		return internalTime;
	}

	public static void setInternalTime(Long internalTime) {
		NewsCache.internalTime = internalTime;
	}

}