package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        log.info("CustomerServiceImpl constructor - loading customer...");
        customerMap = new HashMap<>();

        CustomerDTO duke = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Duke Barrow")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO jake = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName("Jake Lambert")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        CustomerDTO mike = CustomerDTO.builder()
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
    public List<CustomerDTO> getListOfCustomers() {
        log.info("CustomerServiceImpl getListOfCustomers...");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        log.info("CustomerServiceImpl getCustomerById... {}", customerId);
        return Optional.of(customerMap.get(customerId));
    }

    @Override
    public CustomerDTO createACustomer(CustomerDTO customer) {
        log.info("CustomerServiceImpl createACustomer...");
        CustomerDTO savedCustomer = CustomerDTO.builder()
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
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        log.info("CustomerServiceImpl updateCustomer...");
        CustomerDTO existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);
        return Optional.of(existingCustomer);
    }

    @Override
    public Boolean deleteCustomerByID(UUID customerId) {
        log.info("CustomerServiceImpl deleteCustomerByID...");
        if(customerMap.containsKey(customerId)) {
            customerMap.remove(customerId);
            return true;
        }
        return false;

    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        log.info("CustomerServiceImpl patchCustomerById...");
        CustomerDTO existingCustomer = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);

        return Optional.of(existingCustomer);

    }
}
