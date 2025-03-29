package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<Customer> getListOfCustomers();

    Optional<Customer> getCustomerById(UUID customerId);

    Customer createACustomer(Customer customer);

    void updateCustomer(UUID customerId, Customer customer);

    void deleteCustomerByID(UUID customerId);

    Customer patchCustomerById(UUID customerId, Customer customer);
}
