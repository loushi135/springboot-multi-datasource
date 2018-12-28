package com.lsq.springboot.datasource.starter.annotation;

import java.lang.annotation.*;

/**
 * 可用于类和方法
 * 是否使用从库
 * 不带@Slave注解使用主库，
 * 带@Slave注解如果 isSlave为false也使用主库
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Slave {
    boolean isSlave() default true;

    /**
     * 通过参数来定是否slave
     *
     * @return
     */
    String spel() default "";
}
