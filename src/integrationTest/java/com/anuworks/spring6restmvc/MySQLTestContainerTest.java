package com.anuworks.spring6restmvc;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@ActiveProfiles("mysql")
public class MySQLTestContainerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0");

    // Since we are using  @ServiceConnection, we dont have to set the properties
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mysqlContainer::getUsername);
//        registry.add("spring.datasource.password", mysqlContainer::getPassword);
//    }



    @Autowired
    BeerRepo beerRepo;

    @Test
    void testListBeer() {
        List<Beer> beerList = beerRepo.findAll();
        assertThat(beerList.size()).isEqualTo(2413);
    }

}
