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


import com.lsq.springboot.datasource.starter.autoconfigure.DataSourceGroupProperties;
import com.lsq.springboot.datasource.starter.autoconfigure.DataSourceProperty;
import com.lsq.springboot.datasource.starter.autoconfigure.DruidConfig;
import com.lsq.springboot.datasource.starter.autoconfigure.MultiDataSourceProperties;
import com.lsq.springboot.datasource.starter.strategy.DataSourceSelectorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(MultiRoutingDataSource.class);
    public static final String SPLITOR = "#";

    /**
     * 默认datasource,对于没有打上注解的情况 使用默认
     */
    private DataSource primaryDataSource;
    private Map<String, GroupDataSource> groupDataSourceMap = new ConcurrentHashMap<>();
    private static final MultiRoutingDataSource INSTANCE = new MultiRoutingDataSource();


    private MultiRoutingDataSource() {

    }

    @Override
    protected DataSource determineDataSource() {

        String lookupKey = MultiDataSourceContextHolder.getDataSourceLookupKey();
        logger.debug("determineDataSource lookupKey:{}", lookupKey);

        if (!StringUtils.hasText(lookupKey)) {
            return primaryDataSource;
        }

        String temp[] = lookupKey.split(SPLITOR);
        String groupName = temp[0];
        DataSourceTypeEnum dataSourceType = DataSourceTypeEnum.valueOf(temp[1]);

        if (StringUtils.isEmpty(groupName) || null == groupDataSourceMap.get(groupName)) {
            return primaryDataSource;
        }
        return groupDataSourceMap.get(groupName).determineDataSource(dataSourceType);
    }


    public void init(MultiDataSourceProperties multiDataSourceProperties) {
        DruidConfig druidGlobalConfig = multiDataSourceProperties.getDruid();
        Map<String, DataSourceGroupProperties> dataSourceGroupMap = multiDataSourceProperties.getGroup();

        DataSource first = null;
        for (Map.Entry<String, DataSourceGroupProperties> item : dataSourceGroupMap.entrySet()) {
            String groupName = item.getKey().toLowerCase();
            DataSourceGroupProperties groupProperties = item.getValue();
            GroupDataSource groupDataSource = new GroupDataSource();
            groupDataSource.setGroupName(groupName);
            try {
                DataSourceSelectorStrategy strategy = groupProperties.getStrategy().newInstance();
                groupDataSource.setDataSourceSelectorStrategy(strategy);
            } catch (Exception e) {
                throw new RuntimeException("init strategy error:" + groupProperties.getStrategy().getSimpleName() + "," + e.toString());
            }
            if (null == groupProperties.getMaster()) {
                throw new RuntimeException(String.format("datasource group:%s master is null", groupName));
            }

            groupDataSource.setMaster(DataSourceCreator.createDruidDataSource(groupProperties.getMaster(),
                    groupName + "_master", groupProperties.getDruid(), druidGlobalConfig));
            if (groupProperties.getMaster().isPrimary() && primaryDataSource == null) {
                primaryDataSource = groupDataSource.getMaster();
            }
            if (null == first) {
                first = groupDataSource.getMaster();
            }

            if (!CollectionUtils.isEmpty(groupProperties.getSlaves())) {
                int i = 0;
                for (DataSourceProperty dataSourceProperty : groupProperties.getSlaves()) {
                    groupDataSource.getSlaves().add(DataSourceCreator.createDruidDataSource(dataSourceProperty,
                            groupName + "_slave" + i, groupProperties.getDruid(), druidGlobalConfig));
                    i++;
                }
            }
            groupDataSourceMap.put(groupName, groupDataSource);
        }

        //如果没有设置默认datasource 使用第一个master
        if (null == primaryDataSource && null != first) {
            primaryDataSource = first;
        }

        if (null == primaryDataSource) {
            throw new RuntimeException("at least one datasource to config: " + multiDataSourceProperties);
        }
    }

    public DataSource getPrimaryDataSource() {
        return primaryDataSource;
    }

    /**
     * 获取的当前所有的分组数据源
     *
     * @return 当前所有的分组数据源
     */
    public Map<String, GroupDataSource> getGroupDataSourceMap() {
        return groupDataSourceMap;
    }

    public static MultiRoutingDataSource getInstance() {
        return INSTANCE;
    }
}
