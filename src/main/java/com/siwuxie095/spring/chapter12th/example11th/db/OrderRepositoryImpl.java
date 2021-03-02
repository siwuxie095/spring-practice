package com.siwuxie095.spring.chapter12th.example11th.db;

import com.siwuxie095.spring.chapter12th.example11th.domain.Order;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.template.Neo4jOperations;

import java.util.List;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2021-03-02 22:29:31
 */
@SuppressWarnings("all")
public class OrderRepositoryImpl implements OrderOperations {

    private final Neo4jOperations neo4j;

    @Autowired
    public OrderRepositoryImpl(Neo4jOperations neo4j) {
        this.neo4j = neo4j;
    }

    @Override
    public List<Order> findSpringInActionOrders() {
        Result<Map<String, Object>> result = neo4j.query(
                "match (o:Order)-[:HAS_ITEMS]->(i:Item) " +
                        "where i.product='Spring in Action' return o", null);
        return IteratorUtil.asList(result.to(Order.class));
    }

}
