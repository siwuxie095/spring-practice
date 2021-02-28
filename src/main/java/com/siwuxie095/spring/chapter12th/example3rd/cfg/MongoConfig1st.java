package com.siwuxie095.spring.chapter12th.example3rd.cfg;

import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Jiajing Li
 * @date 2021-02-28 18:24:25
 */
@SuppressWarnings("all")
@Configuration
@EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example3rd.db")
public class MongoConfig1st {

    @Bean
    public MongoFactoryBean mongo() {
        MongoFactoryBean mongo = new MongoFactoryBean();
        mongo.setHost("localhost");
        return mongo;
    }

    @Bean
    public MongoOperations mongoTemplete(Mongo mongo) {
        return new MongoTemplate(mongo, "OrdersDB");
    }

}
