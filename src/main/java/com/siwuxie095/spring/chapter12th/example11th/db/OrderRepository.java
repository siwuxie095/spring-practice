package com.siwuxie095.spring.chapter12th.example11th.db;

import com.siwuxie095.spring.chapter12th.example11th.domain.Order;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-02 08:03:29
 */
@SuppressWarnings("all")
public interface OrderRepository extends GraphRepository<Order>, OrderOperations {

    List<Order> findByCustomer(String customer);

    List<Order> findByCustomerLike(String customer);

    List<Order> findByCustomerAndType(String customer, String type);

    List<Order> getByType(String type);

//    @Query("{customer:'Chuck Wagon'}")
//    List<Order> findChucksOrders();

}

