package com.siwuxie095.spring.chapter12th.example3rd.cfg;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

/**
 * @author Jiajing Li
 * @date 2021-02-28 18:57:58
 */
@SuppressWarnings("all")
@Configuration
@EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example3rd.db")
public class MongoConfig3rd extends AbstractMongoConfiguration {

    @Autowired
    private Environment env;

    @Override
    protected String getDatabaseName() {
        return "OrdersDB";
    }

    @Override
    public Mongo mongo() throws Exception {
        MongoCredential credential =
                MongoCredential.createMongoCRCredential(
                        env.getProperty("mongo.username"),
                        getDatabaseName(),
                        env.getProperty("mongo.password").toCharArray());
        return new MongoClient(
                new ServerAddress("localhost", 37017),
                Arrays.asList(credential));
    }

}
