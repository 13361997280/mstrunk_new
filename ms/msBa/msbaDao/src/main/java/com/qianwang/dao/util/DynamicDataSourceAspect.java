package com.qianwang.dao.util;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.qianwang.dao.aspect.DataSource;

@Aspect
@Component
@Order(0)
public class DynamicDataSourceAspect {
	private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	@Before("@annotation(ds)")
	public void before(JoinPoint point, DataSource ds) {
		if (DynamicDataSourceHolder.getManualSwitch()) {
			if (log.isDebugEnabled()) {
				log.debug("==================================manual switch is on");
			}
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("switch ds is {}", ds.ms().get());
		}
		DynamicDataSourceHolder.putDataSource(ds.ms().get());
	}
}
