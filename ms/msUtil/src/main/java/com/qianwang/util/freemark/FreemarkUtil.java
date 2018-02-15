package com.qianwang.util.freemark;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkUtil {
	private static Configuration configuration = null;

	public static void initConfiguration() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		// dataMap 要填入模本的数据文件
		// 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，
		// 这里我们的模板是放在template包下面
		configuration.setClassForTemplateLoading(FreemarkUtil.class,
				"/template");
	}

	public static void writeToOutStr(String ftlName,
			Map<String, Object> dataMap, OutputStream out) {
		
		if (configuration == null) {
			initConfiguration();
		}
		Writer writer = null;
		try {
			 writer = new OutputStreamWriter(out, "UTF-8");
			Template t = configuration.getTemplate(ftlName);

			t.process(dataMap, writer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
