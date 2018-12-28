package com.lsq.springboot.datasource.starter.autoconfigure;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "sppring.datasource.multi")
public class MultiDataSourceProperties extends PrintFriendliness {

    private static final long serialVersionUID = 1430006282261589841L;

    private Map<String, DataSourceGroupProperties> group = new LinkedHashMap<>();

    /**
     * 是否使用p6spy输出，默认不输出
     */
    private Boolean p6spy = false;

    /**
     * 此组下全局默认配置
     */
    @NestedConfigurationProperty
    private DruidConfig druid = new DruidConfig();

    public Map<String, DataSourceGroupProperties> getGroup() {
        return group;
    }

    public void setGroup(Map<String, DataSourceGroupProperties> group) {
        this.group = group;
    }

    public Boolean getP6spy() {
        return p6spy;
    }

    public void setP6spy(Boolean p6spy) {
        this.p6spy = p6spy;
    }

    public DruidConfig getDruid() {
        return druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
    }
}
