

package com.lsq.springboot.test.mybatis;


import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.lsq.springboot.datasource.starter.autoconfigure.MultiDataSourceMatcherConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(exclude = {MybatisPlusAutoConfiguration.class})
@MapperScan(
        basePackages = {
                "com.lsq.springboot.test.mybatis.infrastruture.mapper",
                "com.lsq.springboot.test.mybatis.infrastruture.query"
        })
@ComponentScan("com.lsq.springboot.test.mybatis")
@PropertySource("mybatis/application.properties")
public class Application {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        InputStream in = new ClassPathResource("mybatis/application.properties").getInputStream();
        properties.load(in);
        SpringApplication application = new SpringApplication(Application.class);
        application.setDefaultProperties(properties);

        application.run(args);
    }


    @Bean
    public MultiDataSourceMatcherConfig multiDataSourceMatcherConfig() {
        return MultiDataSourceMatcherConfig.newInstance()
                .expressionMatcher("execution(* com.lsq.springboot.test.mybatis.aop.aspectj.facade.*Facade.*(..))", "testorder")
                .expressionMatcher("execution(* com.lsq.springboot.test.mybatis.aop.aspectj.repository.*Repository.*(..))", "testorder")
                .expressionMatcher("execution(* com.lsq.springboot.test.mybatis.aop.aspectj.repository1.*Repository.*(..))", "testorder1")
                ;
    }
}
