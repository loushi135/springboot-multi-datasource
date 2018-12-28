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

package com.lsq.springboot.datasource.starter.autoconfigure;

import com.lsq.springboot.datasource.starter.strategy.DataSourceSelectorStrategy;
import com.lsq.springboot.datasource.starter.strategy.LoadBalanceDataSourceSelectorStrategy;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

public class DataSourceGroupProperties extends PrintFriendliness {

    private static final long serialVersionUID = 5880722033981434634L;
    private DataSourceProperty master;
    private List<DataSourceProperty> slaves;

    /**
     * 此组下默认配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();
    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
    private Class<? extends DataSourceSelectorStrategy> strategy = LoadBalanceDataSourceSelectorStrategy.class;


    public DataSourceProperty getMaster() {
        return master;
    }

    public void setMaster(DataSourceProperty master) {
        this.master = master;
    }

    public List<DataSourceProperty> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<DataSourceProperty> slaves) {
        this.slaves = slaves;
    }

    public Class<? extends DataSourceSelectorStrategy> getStrategy() {
        return strategy;
    }

    public void setStrategy(Class<? extends DataSourceSelectorStrategy> strategy) {
        this.strategy = strategy;
    }

    public DruidConfig getDruid() {
        return druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
    }
}
