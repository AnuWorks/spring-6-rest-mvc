package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.model.BeerStyle;
import com.anuworks.spring6restmvc.service.BeerService;
import com.anuworks.spring6restmvc.service.BeerServiceImpl;
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
import org.springframework.test.web.servlet.ResultActions;


import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static final String BEER_PATH = "/api/v1/beer";
    private static final String BEER_PATH_ID = "/api/v1/beer/{beerId}";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;


    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testPatchBeerById() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("Hello Beer");

        given(beerService.patchBeerByID(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer));

        mockMvc.perform(patch(BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                        .andExpect(status().isNoContent());

        verify(beerService).patchBeerByID(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(beer.getId());
        assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo(beer.getBeerName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.deleteBeerByID(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(BEER_PATH_ID, beer.getId()))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerByID(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testCreateNewBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);

        beer.setVersion(null);
        beer.setId(UUID.randomUUID());

        given(beerService.createNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().getFirst());

        mockMvc.perform(post(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated());
                //.andExpect(header().exists("Location"));

    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.updateBeerByID(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer));

        mockMvc.perform(put(BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
        .andExpect(status().isNoContent());

        verify(beerService).updateBeerByID(any(UUID.class), any(BeerDTO.class));
    }



    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerList() throws Exception {
        given(beerService.getListOfBeers(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.getListOfBeers(any(), any(), any(), any(), any()));

        mockMvc.perform(get("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));

    }

    // Validation testcases


    @Test
    void testCreateBeerEmptyObject() throws Exception {
        Beer beer = Beer.builder().build();

        ResultActions resultHandlers =  mockMvc.perform(post(BEER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)));

        System.out.println(resultHandlers.andReturn().getResponse().getContentAsString());

    }

    @Test
    void testEmptyUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.getListOfBeers(null, null, false, 1, 25).getContent().get(0);
        BeerDTO emptyBeer = BeerDTO.builder().beerName("Test only beer").beerStyle(BeerStyle.PILSNER).build();

       ResultActions resultActions =  mockMvc.perform(put(BEER_PATH_ID, beer.getId(), emptyBeer)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyBeer)))
                .andExpect(status().isBadRequest());

        System.out.println(resultActions.andReturn().getResponse().getContentAsString());

    }

}