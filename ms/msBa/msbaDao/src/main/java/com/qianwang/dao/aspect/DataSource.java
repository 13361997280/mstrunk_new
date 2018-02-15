package com.qianwang.dao.aspect;

import java.lang.annotation.*;

/**
 *  description
 *
 * @author by zhangchanghong on 15/8/10.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface DataSource {
    String field() default "";

    DbMsEnum ms() default DbMsEnum.S;
}
