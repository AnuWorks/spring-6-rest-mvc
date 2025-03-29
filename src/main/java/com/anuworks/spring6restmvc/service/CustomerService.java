package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> getListOfCustomers();

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    CustomerDTO createACustomer(CustomerDTO customer);

    void updateCustomer(UUID customerId, CustomerDTO customer);

    void deleteCustomerByID(UUID customerId);

    CustomerDTO patchCustomerById(UUID customerId, CustomerDTO customer);
}
