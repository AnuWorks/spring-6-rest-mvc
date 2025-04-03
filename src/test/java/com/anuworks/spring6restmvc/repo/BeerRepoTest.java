package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class BeerRepoTest {

    @Autowired
    BeerRepo beerRepo;

    @Test
    @Transactional
    @Rollback
    void testSaveBeer(){
        assertThrows(ConstraintViolationException.class, () -> {
            Beer beer = beerRepo.save(Beer.builder()
                    .beerName("My BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy BeereeeeeeeeeeeeeeeeeeeeeeMy Beereeeeeeeeeeeeeeeeeeeeee")
                    .beerStyle(BeerStyle.GINGER_BEER)
                    .price(new BigDecimal("25.89"))
                    .upc("114455")
                    .build());

            beerRepo.flush();

            assertThat(beer).isNotNull();
            assertThat(beer.getId()).isNotNull();
        });

    }


}