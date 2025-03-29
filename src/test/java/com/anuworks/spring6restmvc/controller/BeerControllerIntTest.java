package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}