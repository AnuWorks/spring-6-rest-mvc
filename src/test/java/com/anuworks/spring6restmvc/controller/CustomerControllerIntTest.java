package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.entities.Customer;
import com.anuworks.spring6restmvc.model.CustomerDTO;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder().customerName("new customer").build();
        ResponseEntity<CustomerDTO> customer = customerController.createCustomer(customerDTO);

        assertThat(customer.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(customer.getHeaders().getLocation().toString()).isNotEmpty();

        String location = customer.getHeaders().getLocation().toString();
        assertThat(location).isNotNull();
        assertThat(location).isEqualTo(customer.getHeaders().getLocation().toString());
    }
}