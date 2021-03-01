package com.siwuxie095.spring.chapter12th.example8th.cfg;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * @author Jiajing Li
 * @date 2021-03-01 08:14:27
 */
@SuppressWarnings("all")
@Configuration
@EnableNeo4jRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example8th.db")
public class Neo4jConfig1st extends Neo4jConfiguration {

    public Neo4jConfig1st() {
        setBasePackage("com.siwuxie095.spring.chapter12th.example8th");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory()
                .newEmbeddedDatabase("/tmp/graphdb");
    }

}
