package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.entities.BeerOrder;
import com.anuworks.spring6restmvc.entities.BeerOrderShipment;
import com.anuworks.spring6restmvc.entities.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerOrderRepoTest {

    @Autowired
    private BeerOrderRepo beerOrderRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    BeerRepo beerRepo;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepo.findAll().get(0);
        testBeer = beerRepo.findAll().get(0);
    }

    @Test
    @Transactional
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .beerOrderShipment(BeerOrderShipment.builder().trackingNumber("12345r").build())
                .build();

        BeerOrder savedBeerOrder = beerOrderRepo.save(beerOrder);
        System.out.println(savedBeerOrder.getCustomerRef());
    }





}