package com.anuworks.spring6restmvc;

import com.anuworks.spring6restmvc.controller.BeerController;
import com.anuworks.spring6restmvc.controller.NotFoundException;
import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        List<BeerDTO> dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepo.deleteAll();
        List<BeerDTO> dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(0);
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
        beer.setBeerName("UpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdatedUpdated");

        mockMvc.perform(patch("/api/v1/beer/{beerId}", beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest());


    }
}