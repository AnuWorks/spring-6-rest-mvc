package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        log.info("CustomerServiceImpl constructor - loading customer...");
        customerMap = new HashMap<>();

        Customer duke = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Duke Barrow")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer jake = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Jake Lambert")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer mike = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Mike Mall")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(duke.getId(), duke);
        customerMap.put(jake.getId(), jake);
        customerMap.put(mike.getId(), mike);
    }


    @Override
    public List<Customer> listCustomers() {
        log.info("CustomerServiceImpl listCustomers...");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID customerId) {
        log.info("CustomerServiceImpl getCustomerById... {}", customerId);
        return customerMap.get(customerId);
    }
}
