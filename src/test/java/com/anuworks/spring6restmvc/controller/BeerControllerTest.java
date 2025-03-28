package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Beer;
import com.anuworks.spring6restmvc.service.BeerService;
import com.anuworks.spring6restmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void getBeerById() throws Exception {

        Beer testBeer = beerServiceImpl.listBeers().getFirst();
        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}