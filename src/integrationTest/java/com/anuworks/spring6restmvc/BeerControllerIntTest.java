package com.anuworks.spring6restmvc;

import com.anuworks.spring6restmvc.controller.BeerController;
import com.anuworks.spring6restmvc.controller.NotFoundException;
import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.model.BeerStyle;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Tag("integration")
class BeerControllerIntTest {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepo beerRepo;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
            mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testListBeers() {
        Page<BeerDTO> dtos = beerController.getAllBeers(null, null, false, 1, 25);
        assertThat(dtos.getTotalElements()).isEqualTo(2413);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepo.deleteAll();
        Page<BeerDTO> dtos = beerController.getAllBeers(null, null, false, 1, 25);
        assertThat(dtos.getTotalElements()).isEqualTo(0);
    }

    @Test
    void testGetByID() {
        Beer beer = beerRepo.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());
        assertThat(dto.getId()).isEqualTo(beer.getId());
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testSaveBeer() {
        BeerDTO dto = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<BeerDTO> response = beerController.createNewBeer(dto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] locations = response.getHeaders().getLocation().getPath().split("/");
        UUID beerId = UUID.fromString(locations[locations.length - 1]);
        Beer beer = beerRepo.findById(beerId).get();
        assertThat(beer.getId()).isEqualTo(beerId);

    }

    @Test
    @Transactional
    @Rollback
    void testUpdateBeer() {
        Beer beer = beerRepo.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);

        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String name = "Updated";
        beerDTO.setBeerName(name);

        ResponseEntity<HttpStatus> response = beerController.updateByID(beer.getId(), beerDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepo.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(name);

    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.updateByID(UUID.randomUUID(), null));
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteById() {
        Beer beer = beerRepo.findAll().get(0);
        ResponseEntity<HttpStatus> response = beerController.deleteByID(beer.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Optional<Beer> deletedBeer = beerRepo.findById(beer.getId());
        assertThat(deletedBeer).isNotPresent();

    }

    @Test
    void testDeleteByIdNotFoundException() {
        assertThrows(NotFoundException.class, () -> beerController.deleteByID(UUID.randomUUID()));
    }

    @Test
    @Transactional
    @Rollback
    void testPatchBeer() {
        Beer beer = beerRepo.findAll().get(0);
        beer.setBeerName("Updated");
        ResponseEntity<BeerDTO> response = beerController.patchByID(beer.getId(), beerMapper.beerToBeerDTO(beer));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Beer updatedBeer = beerRepo.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beer.getBeerName());
    }

    @Test
    void testPatchNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.patchByID(UUID.randomUUID(), BeerDTO.builder().build()));
    }


    @Test
    void testPatchBeerNameValidation() throws Exception {
        Beer beer = beerRepo.findAll().get(0);
        beer.setBeerName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij");

        mockMvc.perform(patch("/api/v1/beer/{beerId}", beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                        .andExpect(status().isBadRequest());

    }

    @Test
    void testListOfBeerByName() throws Exception {
        mockMvc.perform(get("/api/v1/beer")
                .queryParam("beerName", "%IPA%"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(336)));
    }

    @Test
    void testListOfBeersWithPagination() throws Exception {

        mockMvc.perform(get("/api/v1/beer")
                .queryParam("beerName", "%IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("showInventory", "true")
                .queryParam("pageNumber", "2")
                .queryParam("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(291)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));


    }

    @Disabled // Only for demo
    @Test
    void testUpdateBeerBadVersion() throws Exception {
        Beer beer = beerRepo.findAll().get(0);

        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);

        beerDTO.setBeerName("Updated Name");

        MvcResult result = mockMvc.perform(put("/api/v1/beer/{beerId}", beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

        beerDTO.setBeerName("Updated Name 2");

        MvcResult result2 = mockMvc.perform(put("/api/v1/beer/{beerId}", beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println(result2.getResponse().getStatus());
    }


}