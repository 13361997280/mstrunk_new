package com.qianwang.dao.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.qianwang.dao.util.DataSourceContextHolder;

@Retention(RetentionPolicy.RUNTIME)
public @interface RecentData {

	String dataSourceName() default DataSourceContextHolder.DW_32; 
}
