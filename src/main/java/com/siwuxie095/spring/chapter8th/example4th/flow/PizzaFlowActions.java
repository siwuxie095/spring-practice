package com.siwuxie095.spring.chapter8th.example4th.flow;

import com.siwuxie095.spring.chapter8th.example4th.domain.*;
import com.siwuxie095.spring.chapter8th.example4th.service.CustomerNotFoundException;
import com.siwuxie095.spring.chapter8th.example4th.service.CustomerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.siwuxie095.spring.chapter8th.example4th.domain.PaymentType.CREDIT_CARD;
import static org.apache.log4j.Logger.getLogger;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:56:12
 */
@SuppressWarnings("all")
@Component
public class PizzaFlowActions {

    private static final Logger LOGGER = getLogger(PizzaFlowActions.class);

    @Autowired
    CustomerService customerService;

    public Customer lookupCustomer(String phoneNumber)
            throws CustomerNotFoundException {
        Customer customer = customerService.lookupCustomer(phoneNumber);
        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException();
        }
    }

    public void addCustomer(Customer customer) {
        LOGGER.warn("TODO: Flesh out the addCustomer() method.");
    }

    public Payment verifyPayment(PaymentDetails paymentDetails) {
        Payment payment = null;
        if (paymentDetails.getPaymentType() == CREDIT_CARD) {
            payment = new CreditCardPayment();
        } else {
            payment = new CashOrCheckPayment();
        }

        return payment;
    }

    public void saveOrder(Order order) {
        LOGGER.warn("TODO: Flesh out the saveOrder() method.");
    }

    public boolean checkDeliveryArea(String zipCode) {
        LOGGER.warn("TODO: Flesh out the checkDeliveryArea() method.");
        return "75075".equals(zipCode);
    }

}

