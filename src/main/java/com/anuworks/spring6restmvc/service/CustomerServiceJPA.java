package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.mappers.CustomerMapper;
import com.anuworks.spring6restmvc.model.CustomerDTO;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;
    private final BeerMapper beerMapper;

    @Override
    public List<CustomerDTO> getListOfCustomers() {
        return customerRepo.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDTO)
                .toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return Optional.ofNullable(customerMapper.customerToCustomerDTO(
           customerRepo.findById(customerId).orElse(null)
        ));
    }

    @Override
    public CustomerDTO createACustomer(CustomerDTO customer) {
        return null;
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerDTO customer) {

    }

    @Override
    public void deleteCustomerByID(UUID customerId) {

    }

    @Override
    public CustomerDTO patchCustomerById(UUID customerId, CustomerDTO customer) {
        return null;
    }
}
