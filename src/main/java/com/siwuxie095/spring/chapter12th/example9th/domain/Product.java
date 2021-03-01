package com.siwuxie095.spring.chapter12th.example9th.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * @author Jiajing Li
 * @date 2021-03-01 22:25:42
 */
@SuppressWarnings("all")
@NodeEntity
public class Product {

    @GraphId
    private Long id;

}
