package com.qianwang.dao.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * spring上下文
 * @author zou
 * 
 */
@Service  
public class SpringContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
 
	/**
	 * 返回上下文中注册的Bean
	 * 
	 * @param beanName
	 *            beanName
	 * @return 上下文中注册的Bean
	 */
	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
}
