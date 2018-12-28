

package com.lsq.springboot.test.mybatisplus;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@MapperScan(
        basePackages = {
                "com.lsq.springboot.test.mybatisplus.infrastruture.mapper"
        })
@ComponentScan("com.lsq.springboot.test.mybatisplus")
@PropertySource("mybatisplus/application.properties")
public class Application {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        InputStream in = new ClassPathResource("mybatisplus/application.properties").getInputStream();
        properties.load(in);
        SpringApplication application = new SpringApplication(Application.class);
        application.setDefaultProperties(properties);

        application.run(args);
    }
}
