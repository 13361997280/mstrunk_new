/**
 * 
 */
package com.qianwang.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author wangjg
 *
 */
public class HttpResult{
	private int status ;
	private Map<String,String> headers;
	private byte[] content;
	
	public String getContentType(){
		if(headers==null){
			return null;
		}
		return headers.get("Content-Type");
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getText(String charset){
		if(this.content==null){
			return null;
		}
		try {
			return new String(this.content, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public String toString() {
		return "HttpResult [status=" + status + ", headers=" + headers + ", content=" + content + "]";
	}
}
