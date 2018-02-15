package com.qianwang.web.controller.credit;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qianwang.service.sysconfig.SysconfigService;
import com.qianwang.service.vo.Dict;
import com.qianwang.web.result.AjaxResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianwang.util.HttpResult;
import com.qianwang.util.HttpUtils;
import com.qianwang.web.controller.BaseController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RequestMapping("/credit")
public class UserCreditController extends BaseController{
	@Value("${credit.netty.host}")
	private String host;
	@Autowired
	private SysconfigService sysconfigService;

	@RequestMapping(value="/dict/**")
	@ResponseBody
    public AjaxResult dict(String fieldName) {
		if(StringUtils.isEmpty(fieldName)) return AjaxResult.failureResult("系统key不能为空");
		List<Dict> list = sysconfigService.getDicts(fieldName);
		return AjaxResult.successResult(list);
    }

	@RequestMapping(value="/user/**")
    public void user(HttpServletRequest request, HttpServletResponse response) {
		this.proxy(request, response);
    }
	
	private void proxy(HttpServletRequest request, HttpServletResponse response){
        try {
            String url = request.getServletPath();
            if(url.endsWith(".do")){
            	url = url.substring(0, url.length()-3);
            }
            if(request.getQueryString()!=null&&request.getQueryString().length()>0){
            	url += '?'+request.getQueryString();
				if(url.contains("user/adjust")){
					url = url + "&operator="+getCurrentUsername();
				}
            }
        	url = this.host+url;
        	HttpResult result = null;
        	if("GET".equals(request.getMethod())){
        		result = HttpUtils.getData(url);
        	}else if("POST".equals(request.getMethod())){
        		InputStream in = request.getInputStream();
        		byte[] postData = IOUtils.toByteArray(in);
        		result = HttpUtils.postData(url, postData);
        	}
        	response.setContentType(result.getContentType());
    		OutputStream out = response.getOutputStream();
    		out.write(result.getContent());
    		out.flush();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        
	}
	
	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		return getParameterMap(request.getParameterMap());
	}
	
	public static Map<String, String> getParameterMap(Map<String, String[]> ms) {
		Map<String, String> result = new HashMap<String, String>();
		if (ms == null || ms.size() == 0)
			return result;
		for (Map.Entry<String, String[]> m : ms.entrySet()) {
			result.put(m.getKey(), m.getValue()[0]);
			if(m.getValue().length > 1){
				result.put(m.getKey(), join(m.getValue()));
			}
		}
		return result;
	}
	
	private static String join(String[] ss){
		StringBuilder sb = new StringBuilder();
		for(String s:ss){
			sb.append(s);
			sb.append(',');
		}
		return sb.substring(0, sb.length()-1);
	}
	
}
