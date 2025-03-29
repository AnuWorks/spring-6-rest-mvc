package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Customer;
import com.anuworks.spring6restmvc.service.CustomerService;
import com.anuworks.spring6restmvc.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
         customerServiceImpl = new CustomerServiceImpl();
    }


    @Test
    void testCreateCustomer() throws Exception {

        Customer newCustomer = customerServiceImpl.getListOfCustomers().getFirst();

        given(customerService.createACustomer(any(Customer.class))).willReturn(newCustomer);

        mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(Customer.builder().customerName("Anudeep").build())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));


    }


    @Test
    void getCustomerById() throws Exception {

        Customer testCustomer = customerServiceImpl.getListOfCustomers().getFirst();

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())));

    }

    @Test
    void getCustomerList() throws Exception {
        List<Customer> testCustomers = customerServiceImpl.getListOfCustomers();

        given(customerService.getListOfCustomers()).willReturn(testCustomers);

        mockMvc.perform(get("/api/v1/customer").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }


}