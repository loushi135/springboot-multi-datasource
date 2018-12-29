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
 * 设置数据库组
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DsGroup {

    /**
     * database group name，support spel determine by params args
     * <pre> {@code
     * @DsGroup("testorder")
     * public class OrderServiceImpl extends OrderService {
     *
     *   using slave for group testorder1
     *   @DsGroup("testorder1")
     *   @Slave
     *   @Override
     *   public OrderPo addOrderForSlave() {
     *     return insert();
     *   }
     *
     *   default using master for group testorder
     *   @Override
     *   public OrderPo addOrder() {
     *     return insert();
     *   }
     *
     *   depends on params args to determine group and master or slave
     *   @DsGroup("#dsGroup")
     *   @Slave("#slave")
     *   @Override
     *   public OrderPo addOrderForSpel(String dsGroup,boolean slave) {
     *     return insert();
     *   }
     *
     * }}</pre>
     *
     * @return 数据源名称
     */
    String value();
}
