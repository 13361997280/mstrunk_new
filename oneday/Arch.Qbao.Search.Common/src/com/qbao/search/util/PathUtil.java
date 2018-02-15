package com.qbao.search.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.qbao.search.conf.Config;

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
 * @Create-at 2011-9-13 15:28:18
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-9-13 15:28:18  	li_yao		1.0			Newly created
 */
public class PathUtil {
	//private static final ESLogger logger = Loggers.getLogger(PathUtil.class);
	
	private static final String MS_DATA_PATH_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
	
	private static String DATA_PATH;
	static{
		DATA_PATH = new File(Config.get().get("data.path", "../data/"))
				.getAbsolutePath();
		if(DATA_PATH.charAt(DATA_PATH.length()-1) != File.separatorChar){
			DATA_PATH += File.separatorChar;
		}
	}
	
	public static String getDataPath(){
		return DATA_PATH;
	}
	
	public static String formatPath(long timeStamp) {
		return new SimpleDateFormat(MS_DATA_PATH_FORMAT).format(
				new Date(timeStamp));
	}
	
	public static long parsePath(String timeTag) throws ParseException {
		return new SimpleDateFormat(MS_DATA_PATH_FORMAT).parse(
				timeTag).getTime();
	}
	
	public static String getRelativePath(File file){
		return file.getAbsolutePath().substring(DATA_PATH.length());
	}
	
	/**
	 * 
	 * @param path
	 * @param fileter
	 * @return urlencoded relative file paths joined with ","
	 * @throws UnsupportedEncodingException
	 */
	public static String getRelativePaths(String path) 
		throws UnsupportedEncodingException{
		return getRelativePaths(path, null);
	}
	
	/**
	 * 
	 * @param path
	 * @param fileter
	 * @return urlencoded relative file paths joined with ","
	 * @throws UnsupportedEncodingException
	 */
	public static String getRelativePaths(String path, FilenameFilter fileter) 
			throws UnsupportedEncodingException{
		File dirFile = new File(path);
		if(dirFile.exists() && dirFile.isDirectory()){
			File[] files = dirFile.listFiles(fileter);
			if(files != null && files.length > 0) {
				StringBuilder sb = new StringBuilder(4096);
				for(File file:files){
				   sb.append(PathUtil.getRelativePath(file));
				   sb.append(',');
				}
				sb.setLength(sb.length() - 1);
				return sb.toString();
			}
		}
		return null;
	}
	
	public static String getRebuildIndexPath(String indexName){
		return new StringBuilder().append(DATA_PATH).append(indexName)
		.append("/rebuild/").toString();
	}
	
	public static String getRebuildIndexPath(String indexName, 
			long rebuildTime){
		return getRebuildIndexPath(indexName)+formatPath(rebuildTime) +"/";
	}
	
	public static String getEngineIndexPath(String indexName, long rebuildTime,
			boolean constant){
		String tag = constant ? "/constant/" : "/realtime/";
		return new StringBuilder().append(getEngineIndexPath(indexName))
		.append(formatPath(rebuildTime)).append(tag).toString();
	}
	
	public static String getEngineIndexPath(String indexName){
		return new StringBuilder().append(DATA_PATH).append(indexName)
		.append("/engine/").toString();
	}
	

	/**
	 * @param string
	 * @return
	 */
	public static String getSuggestionPath(String indexName) {
		return new StringBuilder().append(DATA_PATH).append(indexName)
		.append("/suggestion/").toString();
	}
	
	public static String[] getEngineAddrs(){
		return getAddrs("engine", 80);
	}
	
	public static String[] getAddrs(String serverKey, int dftPort){
		String ips = Config.get().get(serverKey + ".ips");
		String[] addrs;
		if(ips != null){
			addrs = ips.split(",");
			int port = Config.get().getInt(serverKey + ".port", dftPort);
			for(int i = 0; i < addrs.length; i++){
				addrs[i] += ":" + port;
			}
		}
		else {
			addrs = new String[]{};
		}
		return addrs;
	}
	
	public static String getRebuildAddr(){
		return getAddr("rebuild", 82);
	}
	
	public static String getScraperAddr(){
		return getAddr("scraper", 83);
	}
	
	public static String getJobAddr(){
		return getAddr("job", 84);
	}
	
	public static String getMonitorAddr(){
		return getAddr("monitor", 86);
	}
	
	public static String getAddr(String serverKey, int dftPort){
		String addr = Config.get().get(serverKey + ".ip");
		if(addr != null){
			addr += ":" + Config.get().getInt(serverKey + ".port", dftPort);
		}
		return addr;
	}
	
	public static String getFileName(String filePath){
		return filePath.substring(
			Math.max(filePath.lastIndexOf('/'),filePath.lastIndexOf('\\')) + 1
		);
	}
}
