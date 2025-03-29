package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.entities.Customer;
import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.mappers.CustomerMapper;
import com.anuworks.spring6restmvc.model.CustomerDTO;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Service
@Primary
@RequiredArgsConstructor
@Slf4j
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
        return customerMapper.customerToCustomerDTO(customerRepo.save(customerMapper.customerDTOToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        log.info("Updating customer " + customerId);
        AtomicReference<Optional<CustomerDTO>> customerRef = new AtomicReference<>();
        customerRepo.findById(customerId).ifPresentOrElse( foundCustomer -> {
            foundCustomer.setCustomerName(customer.getCustomerName());
            foundCustomer.setLastModifiedDate(customer.getLastModifiedDate());
            customerRef.set(
                    Optional.of(
                            customerMapper.customerToCustomerDTO(
                                    customerRepo.save(foundCustomer))));
        },()->{
            customerRef.set(Optional.empty());
        } );
        return customerRef.get();
    }

    @Override
    public Boolean deleteCustomerByID(UUID customerId) {
        if(customerRepo.findById(customerId).isPresent()){
            customerRepo.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> customerRef = new AtomicReference<>();
        customerRepo.findById(customerId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customer.getCustomerName());
            customerRepo.save(foundCustomer);
            customerRef.set(Optional.of(customerMapper.customerToCustomerDTO(foundCustomer)));
        },
                ()->{customerRef.set(Optional.empty());});
    return customerRef.get();
    }
}
