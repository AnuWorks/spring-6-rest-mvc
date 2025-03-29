package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.CustomerDTO;
import com.anuworks.spring6restmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
public class CustomerController {

    private static final String CUSTOMER_PATH = "/api/v1/customer";
    private static final String CUSTOMER_PATH_ID = "/api/v1/customer/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> getListOfCustomer(){
        log.info("CustomerController getListOfCustomer...");
        return customerService.getListOfCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId){
        log.info("CustomerController getCustomerById... {}", customerId);
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        log.info("CustomerController createCustomer...");
        CustomerDTO savedCustomer = customerService.createACustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", savedCustomer.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
        
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        log.info("CustomerController updateCustomer...");

        customerService.updateCustomer(customerId, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> deleteCustomer(@PathVariable("customerId") UUID customerId) {
        log.info("CustomerController deleteCustomer...");

        customerService.deleteCustomerByID(customerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        log.info("CustomerController patchCustomerById...");
        CustomerDTO updatedCustomer = customerService.patchCustomerById(customerId, customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.NO_CONTENT);
    }


}
