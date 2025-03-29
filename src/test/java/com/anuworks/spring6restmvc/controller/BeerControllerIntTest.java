package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIntTest {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepo beerRepo;

    @Autowired
    BeerMapper beerMapper;

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
}