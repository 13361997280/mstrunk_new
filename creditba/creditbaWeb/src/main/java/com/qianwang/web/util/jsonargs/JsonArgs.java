package com.qianwang.web.util.jsonargs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示用json格式requestbody绑定方法的参数
 * 应用于json格式请求
 * @author wangjg
 *
 */
@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonArgs {
	 String value() default "";
}
