package com.siwuxie095.spring.chapter12th.example11th.cfg;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * @author Jiajing Li
 * @date 2021-03-02 08:00:49
 */
@SuppressWarnings("all")
@Configuration
@EnableNeo4jRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example11th.db")
public class Neo4jConfig extends Neo4jConfiguration {

    public Neo4jConfig() {
        setBasePackage("com.siwuxie095.spring.chapter12th.example11th");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory()
                .newEmbeddedDatabase("/tmp/graphdb");
    }

}

