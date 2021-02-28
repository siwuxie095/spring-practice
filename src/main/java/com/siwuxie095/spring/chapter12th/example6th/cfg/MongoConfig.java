package com.siwuxie095.spring.chapter12th.example6th.cfg;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Jiajing Li
 * @date 2021-02-28 20:12:35
 */
@SuppressWarnings("all")
@Configuration
@EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example6th.db",
repositoryImplementationPostfix = "Staff")
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "OrdersDB";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }

}