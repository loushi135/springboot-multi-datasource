/*
 * Copyright 2018 the organization loushi135
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
