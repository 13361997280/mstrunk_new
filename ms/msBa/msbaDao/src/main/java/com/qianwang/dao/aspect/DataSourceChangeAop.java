package com.qianwang.dao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.qianwang.dao.util.DataSourceContextHolder;

@Aspect
@Component
public class DataSourceChangeAop {

	@Around("within(com.qianwang.service..*) && @annotation(dataSource)")
	public Object addLogSuccess(ProceedingJoinPoint jp,RecentData dataSource) throws Throwable {
		DataSourceContextHolder.setDbType(dataSource.dataSourceName());
		Object obj = jp.proceed();
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DW_32);
		return obj;
	}

	// 标注该方法体为异常通知，当目标方法出现异常时，执行该方法体
	@AfterThrowing(pointcut = "within(com.qianwang.service..*) && @annotation(dataSource)", throwing = "ex")
	public void addLog(JoinPoint jp,RecentData dataSource,Exception ex) {
		DataSourceContextHolder.setDbType(DataSourceContextHolder.DW_32);
	}
}
