package com.anuworks.spring6restmvc.bootstrap;

import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import com.anuworks.spring6restmvc.service.BeerCSVService;
import com.anuworks.spring6restmvc.service.BeerCSVServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(BeerCSVServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepo beerRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    BeerCSVService beerCSVService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepo, customerRepo, beerCSVService);
    }

    @Test
    @Transactional
    @Rollback
    void loadData() throws Exception {
        bootstrapData.run((String) null);

        assertThat(beerRepo.count()).isEqualTo(2413);
        assertThat(customerRepo.count()).isEqualTo(3);
    }

}