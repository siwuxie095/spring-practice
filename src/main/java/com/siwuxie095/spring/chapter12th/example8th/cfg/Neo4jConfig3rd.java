package com.siwuxie095.spring.chapter12th.example8th.cfg;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

/**
 * @author Jiajing Li
 * @date 2021-03-01 08:36:18
 */
@SuppressWarnings("all")
@Configuration
@EnableNeo4jRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example8th.db")
public class Neo4jConfig3rd extends Neo4jConfiguration {

    @Autowired
    private Environment env;

    public Neo4jConfig3rd() {
        setBasePackage("com.siwuxie095.spring.chapter12th.example8th");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase(
                "http://graphdbserver:7474/db/data/",
                env.getProperty("db.username"),
                env.getProperty("db.password"));
    }

}
