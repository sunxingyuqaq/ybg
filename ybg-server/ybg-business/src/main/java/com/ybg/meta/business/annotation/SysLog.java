package com.ybg.meta.business.annotation;


import java.lang.annotation.*;

/**
 * @author 12870
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
