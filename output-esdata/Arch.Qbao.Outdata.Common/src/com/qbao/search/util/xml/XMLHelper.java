package com.qbao.search.util.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.HttpRequest;

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
 * @Create-at 2011-11-30 13:31:09
 * 
 * @Modification-history
 * <br>Date					Author		Version		Description
 * <br>----------------------------------------------------------
 * <br>2011-11-30 13:31:09  	li_yao		1.0			Newly created
 */
public class XMLHelper {
	
	public static void output(Document d, OutputStream os) throws IOException{
		output(d, os, true);
	}
	
	public static void output(Document d, OutputStream os, boolean closeOs) 
			throws IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = null;
		try{
			writer = new XMLWriter(os, format);
			writer.write(d);
		} finally{
			try{
				if(closeOs){
					os.close();
				}
			} finally{
				if(writer != null){
					try{
						writer.flush();
					} finally{
						writer.close();
					}
				}
			}
		}
	}
	
	public static void output(Document d, File file) throws IOException{
		output(d, new FileOutputStream(file));
	}
	
	public static String toString(Document d) throws IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		output(d, os);
		return os.toString("UTF-8");
	}
	
	public static Document createResponse(){
		return create("Response", null);
	}
	
	public static Document createRequest(){
		return create("Request", null);
	}
	
	public static Document create(String root, 
			Map<String, String> headerAttributes){
		Document document = create(root);
		Element response = document.getRootElement();
		Element header = response.addElement("Header");
		if(headerAttributes != null){
			for(Entry<String, String> entry:headerAttributes.entrySet()){
				header.addAttribute(entry.getKey(), entry.getValue());
			}
		}
		return document;
	}
	
	public static Document create(String root){
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		document.addElement(root);
		return document;
	}
	
	public static Document create(HttpRequest httpRequest)
			throws DocumentException, IOException{
        return create(new ChannelBufferInputStream(httpRequest.getContent()));
	}
	
	public static Document create(File file) 
			throws DocumentException, IOException{
		return create(new FileInputStream(file), true);
	}
	
	public static Document create(InputStream is) 
			throws DocumentException, IOException{
		return create(is, true);
	}
	
	public static Document create(InputStream is, boolean closeIs) 
			throws DocumentException, IOException{
        SAXReader reader = new SAXReader();
        try{
        	Document doc = reader.read(is);
        	return doc;
        } finally{
        	if(closeIs){
        		is.close();
        	}
        }
	}
	
}
