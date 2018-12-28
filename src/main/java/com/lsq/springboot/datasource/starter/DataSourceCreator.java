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


import com.alibaba.druid.pool.DruidDataSource;
import com.lsq.springboot.datasource.starter.autoconfigure.DataSourceProperty;
import com.lsq.springboot.datasource.starter.autoconfigure.DruidConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Properties;

import static com.alibaba.druid.pool.DruidAbstractDataSource.DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS;
import static com.alibaba.druid.pool.DruidAbstractDataSource.DEFAULT_PHY_TIMEOUT_MILLIS;


public class DataSourceCreator {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceCreator.class);

    /**
     * 创建DRUID数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    static DataSource createDruidDataSource(DataSourceProperty dataSourceProperty, String dataSourceName, DruidConfig druidGroupConfig, DruidConfig druidGlobalConfig) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
        dataSource.setName(dataSourceName);

        DruidConfig[] druidConfigs = new DruidConfig[]{dataSourceProperty.getDruid(), druidGroupConfig, druidGlobalConfig};

        dataSource.configFromPropety(toProperties(druidConfigs));

        String publicKey = dataSourceProperty.getDruid().getPublicKey();
        Properties tempConnectProperties = getPropertyFromMultiSource("connectionProperties", druidConfigs);

        if (publicKey != null && publicKey.length() > 0) {
            if (tempConnectProperties == null) {
                tempConnectProperties = new Properties();
            }
            logger.info("动态数据源-检测到您配置了druid加密,加密所需连接参数已为您自动配置");
            tempConnectProperties.setProperty("config.decrypt", "true");
            tempConnectProperties.setProperty("config.decrypt.key", publicKey);
        }
        //连接参数单独设置
        dataSource.setConnectProperties(tempConnectProperties);

        //设置druid内置properties不支持的的参数
        Boolean testOnReturn = getPropertyFromMultiSource("testOnReturn", druidConfigs);
        if (testOnReturn != null && testOnReturn.equals(true)) {
            dataSource.setTestOnReturn(true);
        }

        Integer validationQueryTimeout = getPropertyFromMultiSource("validationQueryTimeout", druidConfigs);
        if (validationQueryTimeout != null) {
            dataSource.setValidationQueryTimeout(validationQueryTimeout);
        } else {
            dataSource.setValidationQueryTimeout(Integer.valueOf(DataSourceSettingEnum.queryTimeout.getValue()));
        }

        Integer queryTimeout = getPropertyFromMultiSource("queryTimeout", druidConfigs);
        if (queryTimeout != null) {
            dataSource.setQueryTimeout(queryTimeout);
        } else {
            dataSource.setQueryTimeout(Integer.valueOf(DataSourceSettingEnum.queryTimeout.getValue()));
        }

        Integer transactionQueryTimeout = getPropertyFromMultiSource("transactionQueryTimeout", druidConfigs);
        if (transactionQueryTimeout != null) {
            dataSource.setTransactionQueryTimeout(transactionQueryTimeout);
        }else{
            dataSource.setTransactionQueryTimeout(Integer.valueOf(DataSourceSettingEnum.queryTimeout.getValue()));
        }

        Boolean sharePreparedStatements = getPropertyFromMultiSource("sharePreparedStatements", druidConfigs);
        if (sharePreparedStatements != null && sharePreparedStatements.equals(true)) {
            dataSource.setSharePreparedStatements(true);
        }
        Integer connectionErrorRetryAttempts = getPropertyFromMultiSource("connectionErrorRetryAttempts", druidConfigs);
        if (connectionErrorRetryAttempts != null && !connectionErrorRetryAttempts.equals(1)) {
            dataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        }
        Boolean breakAfterAcquireFailure = getPropertyFromMultiSource("breakAfterAcquireFailure", druidConfigs);
        if (breakAfterAcquireFailure != null && breakAfterAcquireFailure.equals(true)) {
            dataSource.setBreakAfterAcquireFailure(true);
        }
        try {
            dataSource.init();
        } catch (SQLException e) {
            logger.error("druid init error,dataSourceProperty:{}", dataSourceProperty, e);
            throw new RuntimeException("druid init error,dataSourceProperty:" + dataSourceProperty);
        }
        return dataSource;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getPropertyFromMultiSource(String property, DruidConfig... druidConfigs) {
        for (DruidConfig config : druidConfigs) {
            try {
                Field field = DruidConfig.class.getDeclaredField(property);
                field.setAccessible(true);
                Object result = field.get(config);
                if (result != null) {
                    return (T) result;
                }
            } catch (Exception e) {
                logger.warn("getPropertyFromMultiSource error", e);
            }
        }
        return null;
    }

    private static Properties toProperties(DruidConfig... druidConfigs) {
        Properties properties = new Properties();

        Integer tempInitialSize = getPropertyFromMultiSource("initialSize", druidConfigs);
        if (tempInitialSize != null) {
            properties.setProperty("druid.initialSize", String.valueOf(tempInitialSize));
        } else {
            properties.setProperty("druid.initialSize", DataSourceSettingEnum.initialSize.getValue());
        }

        Integer tempMaxActive = getPropertyFromMultiSource("maxActive", druidConfigs);
        if (tempMaxActive != null) {
            properties.setProperty("druid.maxActive", String.valueOf(tempMaxActive));
        } else {
            properties.setProperty("druid.maxActive", DataSourceSettingEnum.maxActive.getValue());
        }

        Integer tempMinIdle = getPropertyFromMultiSource("minIdle", druidConfigs);
        if (tempMinIdle != null) {
            properties.setProperty("druid.minIdle", String.valueOf(tempMinIdle));
        } else {
            properties.setProperty("druid.minIdle", DataSourceSettingEnum.minIdle.getValue());
        }

        Long tempMaxWait = getPropertyFromMultiSource("maxWait", druidConfigs);
        if (tempMaxWait != null) {
            properties.setProperty("druid.maxWait", String.valueOf(tempMaxWait));
        } else {
            properties.setProperty("druid.maxWait", DataSourceSettingEnum.maxWait.getValue());
        }

        Long tempTimeBetweenEvictionRunsMillis = getPropertyFromMultiSource("timeBetweenEvictionRunsMillis", druidConfigs);
        if (tempTimeBetweenEvictionRunsMillis != null) {
            properties.setProperty("druid.timeBetweenEvictionRunsMillis", String.valueOf(tempTimeBetweenEvictionRunsMillis));
        } else {
            properties.setProperty("druid.timeBetweenEvictionRunsMillis", DataSourceSettingEnum.timeBetweenEvictionRunsMillis.getValue());
        }

        Long tempTimeBetweenLogStatsMillis = getPropertyFromMultiSource("timeBetweenLogStatsMillis", druidConfigs);
        if (tempTimeBetweenLogStatsMillis != null && tempTimeBetweenLogStatsMillis > 0) {
            properties.setProperty("druid.timeBetweenLogStatsMillis", String.valueOf(tempTimeBetweenLogStatsMillis));
        }

        Integer tempStatSqlMaxSize = getPropertyFromMultiSource("statSqlMaxSize", druidConfigs);
        if (tempStatSqlMaxSize != null) {
            properties.setProperty("druid.stat.sql.MaxSize", String.valueOf(tempStatSqlMaxSize));
        }

        Long tempMinEvictableIdleTimeMillis = getPropertyFromMultiSource("minEvictableIdleTimeMillis", druidConfigs);
        if (tempMinEvictableIdleTimeMillis != null) {
            properties.setProperty("druid.minEvictableIdleTimeMillis", String.valueOf(tempMinEvictableIdleTimeMillis));
        } else {
            properties.setProperty("druid.minEvictableIdleTimeMillis", DataSourceSettingEnum.minEvictableIdleTimeMillis.getValue());
        }

        Long tempMaxEvictableIdleTimeMillis = getPropertyFromMultiSource("maxEvictableIdleTimeMillis", druidConfigs);
        if (tempMaxEvictableIdleTimeMillis != null && !tempMaxEvictableIdleTimeMillis.equals(DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS)) {
            properties.setProperty("druid.maxEvictableIdleTimeMillis", String.valueOf(tempMaxEvictableIdleTimeMillis));
        }

        Boolean tempTestWhileIdle = getPropertyFromMultiSource("testWhileIdle", druidConfigs);
        if (tempTestWhileIdle != null) {
            properties.setProperty("druid.testWhileIdle", String.valueOf(tempTestWhileIdle));
        } else {
            properties.setProperty("druid.testWhileIdle", DataSourceSettingEnum.testWhileIdle.getValue());
        }

        Boolean tempTestOnBorrow = getPropertyFromMultiSource("testOnBorrow", druidConfigs);
        if (tempTestOnBorrow != null) {
            properties.setProperty("druid.testOnBorrow", String.valueOf(tempTestOnBorrow));
        } else {
            properties.setProperty("druid.testOnBorrow", DataSourceSettingEnum.testOnBorrow.getValue());
        }

        String tempValidationQuery = getPropertyFromMultiSource("validationQuery", druidConfigs);
        if (tempValidationQuery != null && tempValidationQuery.length() > 0) {
            properties.setProperty("druid.validationQuery", tempValidationQuery);
        } else {
            properties.setProperty("druid.validationQuery", DataSourceSettingEnum.validationQuery.getValue());
        }

        Boolean tempUseGlobalDataSourceStat = getPropertyFromMultiSource("useGlobalDataSourceStat", druidConfigs);
        if (tempUseGlobalDataSourceStat != null && tempUseGlobalDataSourceStat.equals(Boolean.TRUE)) {
            properties.setProperty("druid.useGlobalDataSourceStat", "true");
        }

        Boolean tempAsyncInit = getPropertyFromMultiSource("asyncInit", druidConfigs);
        if (tempAsyncInit != null && tempAsyncInit.equals(Boolean.TRUE)) {
            properties.setProperty("druid.asyncInit", "true");
        }

        //filters单独处理，默认了stat,wall
        String tempFilters = getPropertyFromMultiSource("filters", druidConfigs);

        if (tempFilters == null) {
            tempFilters = "stat,wall";
        }
        String publicKey = druidConfigs[0].getPublicKey();
        if (publicKey != null && publicKey.length() > 0 && !tempFilters.contains("config")) {
            tempFilters += ",config";
        }
        properties.setProperty("druid.filters", tempFilters);

        Boolean tempClearFiltersEnable = getPropertyFromMultiSource("clearFiltersEnable", druidConfigs);
        if (tempClearFiltersEnable != null && tempClearFiltersEnable.equals(Boolean.FALSE)) {
            properties.setProperty("druid.clearFiltersEnable", "false");
        }

        Boolean tempResetStatEnable = getPropertyFromMultiSource("resetStatEnable", druidConfigs);
        if (tempResetStatEnable != null && tempResetStatEnable.equals(Boolean.FALSE)) {
            properties.setProperty("druid.resetStatEnable", "false");
        }

        Integer tempNotFullTimeoutRetryCount = getPropertyFromMultiSource("notFullTimeoutRetryCount", druidConfigs);
        if (tempNotFullTimeoutRetryCount != null && !tempNotFullTimeoutRetryCount.equals(0)) {
            properties.setProperty("druid.notFullTimeoutRetryCount", String.valueOf(tempNotFullTimeoutRetryCount));
        }

        Integer tempMaxWaitThreadCount = getPropertyFromMultiSource("maxWaitThreadCount", druidConfigs);
        if (tempMaxWaitThreadCount != null && !tempMaxWaitThreadCount.equals(-1)) {
            properties.setProperty("druid.maxWaitThreadCount", String.valueOf(tempMaxWaitThreadCount));
        }

        Boolean tempFailFast = getPropertyFromMultiSource("failFast", druidConfigs);
        if (tempFailFast != null && tempFailFast.equals(Boolean.TRUE)) {
            properties.setProperty("druid.failFast", "true");
        }

        Integer tempPhyTimeoutMillis = getPropertyFromMultiSource("phyTimeoutMillis", druidConfigs);
        if (tempPhyTimeoutMillis != null && !tempPhyTimeoutMillis.equals(DEFAULT_PHY_TIMEOUT_MILLIS)) {
            properties.setProperty("druid.phyTimeoutMillis", String.valueOf(tempPhyTimeoutMillis));
        }

        Boolean tempKeepAlive = getPropertyFromMultiSource("keepAlive", druidConfigs);
        if (tempKeepAlive != null && tempKeepAlive.equals(Boolean.TRUE)) {
            properties.setProperty("druid.keepAlive", "true");
        }

        Boolean tempPoolPreparedStatements = getPropertyFromMultiSource("poolPreparedStatements", druidConfigs);
        if (tempPoolPreparedStatements != null && tempPoolPreparedStatements.equals(Boolean.TRUE)) {
            properties.setProperty("druid.poolPreparedStatements", "true");
        }

        Boolean tempInitVariants = getPropertyFromMultiSource("initVariants", druidConfigs);
        if (tempInitVariants != null && tempInitVariants.equals(Boolean.TRUE)) {
            properties.setProperty("druid.initVariants", "true");
        }

        Boolean tempInitGlobalVariants = getPropertyFromMultiSource("initGlobalVariants", druidConfigs);
        if (tempInitGlobalVariants != null && tempInitGlobalVariants.equals(Boolean.TRUE)) {
            properties.setProperty("druid.initGlobalVariants", "true");
        }

        Boolean tempUseUnfairLock = getPropertyFromMultiSource("useUnfairLock", druidConfigs);
        if (tempUseUnfairLock != null) {
            properties.setProperty("druid.useUnfairLock", String.valueOf(tempUseUnfairLock));
        }

        Boolean tempKillWhenSocketReadTimeout = getPropertyFromMultiSource("killWhenSocketReadTimeout", druidConfigs);
        if (tempKillWhenSocketReadTimeout != null && tempKillWhenSocketReadTimeout.equals(Boolean.TRUE)) {
            properties.setProperty("druid.killWhenSocketReadTimeout", "true");
        }

        Integer tempMaxPoolPreparedStatementPerConnectionSize = getPropertyFromMultiSource("maxPoolPreparedStatementPerConnectionSize", druidConfigs);
        if (tempMaxPoolPreparedStatementPerConnectionSize != null && !tempMaxPoolPreparedStatementPerConnectionSize.equals(10)) {
            properties.setProperty("druid.maxPoolPreparedStatementPerConnectionSize", String.valueOf(tempMaxPoolPreparedStatementPerConnectionSize));
        }

        String tempInitConnectionSqls = getPropertyFromMultiSource("initConnectionSqls", druidConfigs);
        if (tempInitConnectionSqls != null && tempInitConnectionSqls.length() > 0) {
            properties.setProperty("druid.initConnectionSqls", tempInitConnectionSqls);
        }

        return properties;
    }
}
