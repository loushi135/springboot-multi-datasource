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

package com.lsq.springboot.datasource.starter;


public enum DataSourceSettingEnum {

    initialSize("20"),

    minIdle("5"),

    maxActive("100"),

    /**
     * 配置获取连接等待超时的时间
     */
    maxWait("60000"),
    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    timeBetweenEvictionRunsMillis("60000"),

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    minEvictableIdleTimeMillis("300000"),

    validationQuery("SELECT 'x'"),

    testWhileIdle("true"),

    testOnBorrow("false"),
    /**
     * datasource 单位秒
     * 查询timeout throws SQLException;
     */
    queryTimeout("30");

    String value;

    DataSourceSettingEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
