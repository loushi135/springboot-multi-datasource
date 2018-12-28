package com.lsq.springboot.datasource.starter.annotation;

import java.lang.annotation.*;

/**
 * 可用于类和方法
 * 设置数据库组
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DsGroup {

    /**
     * 组名
     *
     * @return 数据源名称
     */
    String value();
}
