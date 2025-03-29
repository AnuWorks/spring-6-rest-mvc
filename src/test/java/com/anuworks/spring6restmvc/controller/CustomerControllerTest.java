package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Customer;
import com.anuworks.spring6restmvc.service.CustomerService;
import com.anuworks.spring6restmvc.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void testDeleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.getListOfCustomers().getFirst();

        mockMvc.perform(delete("/api/v1/customer/" + customer.getId()))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(customerService).deleteCustomerByID(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.getListOfCustomers().getFirst();

        mockMvc.perform(put("/api/v1/customer/" + customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomer(any(UUID.class), any(Customer.class));
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