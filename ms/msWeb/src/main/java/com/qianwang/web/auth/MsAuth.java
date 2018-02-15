package com.qianwang.web.auth;

import java.lang.annotation.*;

/**
 * @author song.j
 * @create 2017-08-21 14:14:23
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MsAuth {
    /**
     * @return
     */
    CheckEnum role();

    /**
     * @return
     */
    String url() default "";
}
