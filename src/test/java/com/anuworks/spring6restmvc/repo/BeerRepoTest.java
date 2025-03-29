package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.bootstrap.BootstrapData;
import com.anuworks.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepoTest {

    @Autowired
    BeerRepo beerRepo;

    @Test
    void testSaveBeer(){
        Beer beer = beerRepo.save(Beer.builder()
                        .beerName("My Beer")
                .build());

        assertThat(beer).isNotNull();
        assertThat(beer.getId()).isNotNull();
    }


}