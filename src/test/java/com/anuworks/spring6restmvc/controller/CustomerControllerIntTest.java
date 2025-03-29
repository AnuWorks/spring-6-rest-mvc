package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.entities.Customer;
import com.anuworks.spring6restmvc.model.CustomerDTO;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIntTest {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepo customerRepo;


    @Test
    void testGetListOfCustomers() {
        List<CustomerDTO> customers = customerController.getListOfCustomer();
        assertThat(customers.size()).isEqualTo(3);

    }

    @Transactional
    @Rollback
    @Test
    void testGetListOfCustomersEmpty() {
        customerRepo.deleteAll();
        List<CustomerDTO> customer = customerController.getListOfCustomer();
        assertThat(customer.size()).isEqualTo(0);
    }

    @Test
    void testGetByID() {
        Customer customer = customerRepo.findAll().getFirst();
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertThat(customerDTO.getId()).isEqualTo(customer.getId());
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, ()-> customerController.getCustomerById(UUID.randomUUID()));
    }
}