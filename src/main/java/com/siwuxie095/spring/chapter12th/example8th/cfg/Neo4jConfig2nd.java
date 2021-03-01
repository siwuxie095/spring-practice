package com.siwuxie095.spring.chapter12th.example8th.cfg;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

/**
 * @author Jiajing Li
 * @date 2021-03-01 08:33:02
 */
@SuppressWarnings("all")
@Configuration
@EnableNeo4jRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example8th.db")
public class Neo4jConfig2nd extends Neo4jConfiguration {

    public Neo4jConfig2nd() {
        setBasePackage("com.siwuxie095.spring.chapter12th.example8th");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase(
                "http://graphdbserver:7474/db/data/");
    }

}
