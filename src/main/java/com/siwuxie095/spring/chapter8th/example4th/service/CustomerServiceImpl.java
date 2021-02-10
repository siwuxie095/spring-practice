package com.siwuxie095.spring.chapter8th.example4th.service;

import com.siwuxie095.spring.chapter8th.example4th.domain.Customer;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:37:51
 */
@SuppressWarnings("all")
public class CustomerServiceImpl implements CustomerService {

    public CustomerServiceImpl() {
    }

    @Override
    public Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException {
        if ("9725551234".equals(phoneNumber)) {
            Customer customer = new Customer();
            customer.setId(123);
            customer.setName("Craig Walls");
            customer.setAddress("3700 Dunlavy Rd");
            customer.setCity("Denton");
            customer.setState("TX");
            customer.setZipCode("76210");
            customer.setPhoneNumber(phoneNumber);
            return customer;
        } else {
            throw new CustomerNotFoundException();
        }
    }

}

