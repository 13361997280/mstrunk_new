/**
 * 
 */
package com.qianwang.credit.util.page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PageParam {

	String pageNo() default "pageNo";

	String pageSize() default "pageSize";

	String rowTotal() default "rowTotal";

}
