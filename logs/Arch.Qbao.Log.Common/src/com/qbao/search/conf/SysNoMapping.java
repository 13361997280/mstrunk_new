package com.qbao.search.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SysNoMapping {
	
	private final static Map<Integer,Integer>  sysNoMapping = getSysNoMapping();
	
	private static Map<Integer,Integer> getSysNoMapping(){
		Map<Integer,Integer> sysno=new HashMap<Integer,Integer>();
		sysno.put(901002,9206);
		sysno.put(901003,9211);
		sysno.put(901004,9212);	
		sysno.put(901006,9212);
		try {
			String mapStr = Config.getBase().get("sysNo.mapping", "").trim();
			if(!mapStr.isEmpty()) {
				if(mapStr.indexOf(':') == -1) {
					int baseSysNo = Config.getBase().getInt("sysno", 0);
					if(baseSysNo == 0) {
						throw new IllegalArgumentException("The sysno has not" +
							" be configured in the loadConfig.properties yet!");
					}
					sysno.put(baseSysNo, Integer.parseInt(mapStr));
				} else {//backward compatibility 
					for(String kvStr:mapStr.split(",")) {
						String[] kv = kvStr.split(":");
						sysno.put(Integer.parseInt(kv[0].trim()), 
								Integer.parseInt(kv[1].trim()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sysno;
	}
	public static int getNewSysNo(int sysNo){
		 
		int newSysNo=0;
		if(sysNo>1000000){
			
			int No= sysNo/100;
			int subNo=sysNo%100;
			
			Integer newNo=sysNoMapping.get(No);
			if(newNo == null) {
				throw new IllegalArgumentException("The mapping sysNo of " + 
					No + " has not be configured in the loadConfig.properties yet");
			}
			
			newSysNo=newNo*100+subNo;
		}
		return newSysNo;
	}
}
