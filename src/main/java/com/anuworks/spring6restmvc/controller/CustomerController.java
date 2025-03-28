package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Customer;
import com.anuworks.spring6restmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Slf4j
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getListOfCustomer(){
        log.info("CustomerController getListOfCustomer...");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId){
        log.info("CustomerController getCustomerById... {}", customerId);
        return customerService.getCustomerById(customerId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        log.info("CustomerController createCustomer...");
        Customer savedCustomer = customerService.addCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", savedCustomer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
        
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer( @PathVariable UUID customerId, @RequestBody Customer customer) {
        log.info("CustomerController updateCustomer...");

        customerService.updateCustomer(customerId, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
