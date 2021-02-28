package com.siwuxie095.spring.chapter12th.example6th.db;

import com.siwuxie095.spring.chapter12th.example6th.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-28 20:11:48
 */
@SuppressWarnings("all")
public interface OrderRepository extends MongoRepository<Order, String>, OrderOperations {

    List<Order> findByCustomer(String customer);

    List<Order> findByCustomerLike(String customer);

    List<Order> findByCustomerAndType(String customer, String type);

    List<Order> getByType(String type);

    @Query("{customer:'Chuck Wagon'}")
    List<Order> findChucksOrders();

}