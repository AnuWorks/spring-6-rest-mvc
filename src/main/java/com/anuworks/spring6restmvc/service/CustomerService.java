package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();
    Customer getCustomerById(UUID customerId);

}
