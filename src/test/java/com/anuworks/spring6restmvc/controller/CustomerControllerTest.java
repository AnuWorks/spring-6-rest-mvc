package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Customer;
import com.anuworks.spring6restmvc.service.CustomerService;
import com.anuworks.spring6restmvc.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
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

    private static final String CUSTOMER_PATH = "/api/v1/customer";
    private static final String CUSTOMER_PATH_ID = "/api/v1/customer/{customerId}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    private CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
         customerServiceImpl = new CustomerServiceImpl();
    }


    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerServiceImpl.getListOfCustomers().getFirst();
        customer.setCustomerName("This is for test patch");

        mockMvc.perform(patch(CUSTOMER_PATH_ID, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getCustomerName()).isEqualTo(customer.getCustomerName());

    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.getListOfCustomers().getFirst();

        mockMvc.perform(delete(CUSTOMER_PATH_ID, customer.getId()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerByID(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.getListOfCustomers().getFirst();

        mockMvc.perform(put(CUSTOMER_PATH_ID, customer.getId())
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

        mockMvc.perform(post(CUSTOMER_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(Customer.builder().customerName("Anudeep").build())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }


    @Test
    void getCustomerById() throws Exception {

        Customer testCustomer = customerServiceImpl.getListOfCustomers().getFirst();

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CUSTOMER_PATH_ID, testCustomer.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())));

    }

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerList() throws Exception {
        List<Customer> testCustomers = customerServiceImpl.getListOfCustomers();

        given(customerService.getListOfCustomers()).willReturn(testCustomers);

        mockMvc.perform(get(CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }


}