package com.lsq.springboot.datasource.starter.autoconfigure;

import com.lsq.springboot.datasource.starter.MultiRoutingDataSource;
import com.lsq.springboot.datasource.starter.aspect.MultiDataSourceAdvisor;
import com.lsq.springboot.datasource.starter.aspect.MultiDataSourceAnnotationAdvisor;
import com.lsq.springboot.datasource.starter.aspect.MultiDataSourceAnnotationInterceptor;
import com.p6spy.engine.spy.P6DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({MultiDataSourceProperties.class})
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class MultiDataSourceAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MultiDataSourceAutoConfiguration.class);

    @Autowired
    private MultiDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        logger.info("loading multi datasource properties:{}", properties);
        MultiRoutingDataSource dataSource = MultiRoutingDataSource.getInstance();
        dataSource.init(properties);
        if (properties.getP6spy()) {
            try {
                Class.forName("com.p6spy.engine.spy.P6DataSource");
                logger.info("动态数据源-关联p6sy成功");
                return new P6DataSource(dataSource);
            } catch (Exception e) {
                logger.warn("多数据源启动器开启了p6spy但并未引入相关依赖");
            }
        }
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public MultiDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        MultiDataSourceAnnotationInterceptor interceptor = new MultiDataSourceAnnotationInterceptor();
        MultiDataSourceAnnotationAdvisor advisor = new MultiDataSourceAnnotationAdvisor(interceptor);
        return advisor;
    }


    @Bean
    @ConditionalOnBean(MultiDataSourceMatcherConfig.class)
    public MultiDataSourceAdvisor multiDataSourceAdvisor(MultiDataSourceMatcherConfig multiDataSourceMatcherConfig) {
        return new MultiDataSourceAdvisor(multiDataSourceMatcherConfig.getMatchers());
    }

}
