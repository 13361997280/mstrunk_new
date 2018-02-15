package com.qbao.search.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
 * @Create-at 2012-3-20 15:32:12
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2012-3-20 15:32:12  	li_yao		1.0			Newly created
 */
public class IndexName {
	
	public static String get(){
		String osName = System.getProperty("os.name");
		if(osName != null && osName.toLowerCase().contains("windows")) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("../index.name"));
				return br.readLine();
			} catch (IOException e) {
				System.out.println(e);
				return "markland";
			} finally {
				if(br != null) {
					try {
						br.close();
					} catch (IOException e) {
						System.out.println(e);
					}
				}
			}
		}
		return new File(System.getProperty("user.dir")).getParentFile().getName();
	}
	
	public static String get(String name){
		String osName = System.getProperty("os.name");
		if(osName != null && osName.toLowerCase().contains("windows")) {
			return name;
		}
		return new File(System.getProperty("user.dir")).getParentFile().getName();
	}
	
	public static void main(String[] args) {
		System.out.println(IndexName.get());
	}
}
