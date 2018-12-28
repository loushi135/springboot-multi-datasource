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

import com.google.common.collect.Lists;
import com.lsq.springboot.datasource.starter.strategy.DataSourceSelectorStrategy;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

public class GroupDataSource {

    private String groupName;
    private DataSource master;
    private List<DataSource> slaves = Lists.newArrayList();

    /**
     * 数据源切换策略
     */
    private DataSourceSelectorStrategy dataSourceSelectorStrategy;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public DataSource getMaster() {
        return master;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public List<DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<DataSource> slaves) {
        this.slaves = slaves;
    }

    public DataSourceSelectorStrategy getDataSourceSelectorStrategy() {
        return dataSourceSelectorStrategy;
    }

    public void setDataSourceSelectorStrategy(DataSourceSelectorStrategy dataSourceSelectorStrategy) {
        this.dataSourceSelectorStrategy = dataSourceSelectorStrategy;
    }

    public DataSource determineDataSource(DataSourceTypeEnum dataSourceType) {
        switch (dataSourceType) {
            case MASTER:
                return getMaster();
            case SLAVE:
                if (CollectionUtils.isEmpty(getSlaves())) {
                    return getMaster();
                }
                if (getSlaves().size() == 1) {
                    return getSlaves().get(0);
                }
                return dataSourceSelectorStrategy.determineDataSource(getSlaves());
            default:
                throw new IllegalStateException("unknow datasource type:" + dataSourceType.name());
        }
    }

}
