package com.siwuxie095.spring.chapter8th.example4th.service;

import com.siwuxie095.spring.chapter8th.example4th.domain.Customer;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:37:12
 */
@SuppressWarnings("all")
public interface CustomerService {

    Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException;

}
