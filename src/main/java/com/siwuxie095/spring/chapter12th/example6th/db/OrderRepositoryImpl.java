package com.siwuxie095.spring.chapter12th.example6th.db;

import com.siwuxie095.spring.chapter12th.example6th.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-28 20:50:33
 */
@SuppressWarnings("all")
public class OrderRepositoryImpl implements OrderOperations {

    @Autowired
    private MongoOperations mongo;

    @Override
    public List<Order> findOrdersByType(String type) {
        String newType = type.equals("NET") ? "WEB" : type;
        Criteria criteria = Criteria.where("type").is(newType);
        Query query = Query.query(criteria);
        return mongo.find(query, Order.class);
    }

}
