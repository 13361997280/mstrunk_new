package com.qbao.search.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class LifeCycleUtil {
	private static PrintWriter pw = null;
	private static String componentName = null;
	private static SimpleDateFormat sdfts = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	private static Map<String,String> recordMap = new ConcurrentHashMap<String,String>();
	public static void init(String name) throws IOException{
		componentName = name;
		generateLifecycleFile();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		pw.println("Lifecycle init, the time is:"+sdfts.format(cal.getTime()));
	}
	
	private static void generateLifecycleFile() throws IOException{
		int pid = getPid();
		File dir = new File("");
		String dirName = dir.getAbsolutePath();
		if(dirName.endsWith(File.separator)){
			dirName = dirName + "lifecycle";
		}else{
			dirName = dirName + File.separator + "lifecycle";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		dirName = dirName + File.separator + sdf.format(cal.getTime()) + File.separator;
		dir = new File(dirName);
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(componentName != null){
			componentName = componentName.replace(" ", "-");
		}else{
			componentName = "UNKNOW";
		}
		String recordFileName = dirName + componentName + "_" + pid + ".log";
		File recordFile = new File(recordFileName);
		if(!recordFile.exists()){
			recordFile.createNewFile();
		}
		pw = new PrintWriter(new FileOutputStream(recordFile,true),true);
	}
	
	public static int getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format: "pid@hostname"
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1;
        }
    }
	
	public static void putShutdownRecordPropertie(String key,String value){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		recordMap.put(key, "["+sdfts.format(cal.getTime())+"]"+value);
	}
	
	public static void printConfig(Properties properties) throws FileNotFoundException{
		if(pw != null){
			pw.println();
			pw.println("The config is:");
			Iterator<Entry<Object,Object>> it = properties.entrySet().iterator();
			while(it.hasNext()){
				Entry<Object,Object> entry = it.next();
				pw.println(entry.getKey()+":"+entry.getValue());
			}
			pw.println();
		}
	}
	
	public static void registerShutdownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				if(pw != null){
					try{
						pw.println("The system start shut down...");
						pw.flush();
					}catch(Exception e){
						try {
							generateLifecycleFile();
						} catch (IOException ex) {

						}
					}
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(System.currentTimeMillis());
					pw.println("The time is:"+sdfts.format(cal.getTime()));
					pw.println("The record properties is:");
					Iterator<Entry<String,String>> it = recordMap.entrySet().iterator();
					while(it.hasNext()){
						Entry<String,String> entry = it.next();
						pw.println(entry.getKey()+":"+entry.getValue());
					}
					pw.println("Shutdown hook executed.");
					pw.close();
				}
			}
		});
	}
	
}
