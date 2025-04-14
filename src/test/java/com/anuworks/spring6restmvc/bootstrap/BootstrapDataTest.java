package com.anuworks.spring6restmvc.bootstrap;

import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import com.anuworks.spring6restmvc.service.BeerCSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class BootstrapDataTest {

    @Autowired
    private BeerRepo beerRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BeerCSVService beerCSVService;

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

        assertThat(beerRepo.count()).isEqualTo(3);
        assertThat(customerRepo.count()).isEqualTo(3);
    }

}