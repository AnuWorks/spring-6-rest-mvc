package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.html.Option;
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
    public List<Customer> getListOfCustomers() {
        log.info("CustomerServiceImpl getListOfCustomers...");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<Customer> getCustomerById(UUID customerId) {
        log.info("CustomerServiceImpl getCustomerById... {}", customerId);
        return Optional.of(customerMap.get(customerId));
    }

    @Override
    public Customer createACustomer(Customer customer) {
        log.info("CustomerServiceImpl createACustomer...");
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customerMap.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;

    }

    @Override
    public void updateCustomer(UUID customerId, Customer customer) {
        log.info("CustomerServiceImpl updateCustomer...");
        Customer existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);

    }

    @Override
    public void deleteCustomerByID(UUID customerId) {
        log.info("CustomerServiceImpl deleteCustomerByID...");
        customerMap.remove(customerId);
    }

    @Override
    public Customer patchCustomerById(UUID customerId, Customer customer) {
        log.info("CustomerServiceImpl patchCustomerById...");
        Customer existingCustomer = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);

        return existingCustomer;

    }
}
