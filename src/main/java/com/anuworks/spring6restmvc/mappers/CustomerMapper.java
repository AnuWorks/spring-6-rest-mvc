package com.anuworks.spring6restmvc.mappers;

import com.anuworks.spring6restmvc.entities.Customer;
import com.anuworks.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
