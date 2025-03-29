package com.anuworks.spring6restmvc.bootstrap;

import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BootstrapDataTest {

    @Autowired
    private BeerRepo beerRepo;

    @Autowired
    private CustomerRepo customerRepo;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepo, customerRepo);
    }

    @Test
    void loadData() throws Exception {
        bootstrapData.run(null);

        assertThat(beerRepo.count()).isEqualTo(3);
        assertThat(customerRepo.count()).isEqualTo(3);
    }

}