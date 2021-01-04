package com.siwuxie095.spring.chapter3rd.example3rd.cfg;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * @author Jiajing Li
 * @date 2021-01-04 08:40:22
 */
@SuppressWarnings("all")
@Configuration
@Profile("qa")
public class TestProfileConfig {

    @Bean(destroyMethod = "close")
    public DataSource testDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:h2:tcp://dbserver/~/test");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        dataSource.setInitialSize(20);
        dataSource.setMaxActive(30);
        return dataSource;
    }

}
