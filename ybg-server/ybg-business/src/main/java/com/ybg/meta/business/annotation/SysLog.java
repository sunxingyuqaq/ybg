package com.ybg.meta.business.annotation;


import java.lang.annotation.*;

/**
 * @author admin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    /**
     * 操作类型
     *
     * @return
     */
    int logType() default 1;

}
