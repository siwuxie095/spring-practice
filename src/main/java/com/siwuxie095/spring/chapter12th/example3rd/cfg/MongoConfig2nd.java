package com.siwuxie095.spring.chapter12th.example3rd.cfg;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Jiajing Li
 * @date 2021-02-28 18:36:36
 */
@SuppressWarnings("all")
@Configuration
@EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example3rd.db")
public class MongoConfig2nd extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "OrdersDB";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }

}
