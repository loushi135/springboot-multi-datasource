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
