package com.anuworks.spring6restmvc.bootstrap;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.entities.Customer;
import com.anuworks.spring6restmvc.model.BeerStyle;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final BeerRepo beerRepo;

    private final CustomerRepo customerRepo;

    @Override
    public void run(String... args) throws Exception {
        log.debug("Starting Bootstrap Data...");
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.LAGER)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.GINGER_BEER)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        beerRepo.save(beer1);
        beerRepo.save(beer2);
        beerRepo.save(beer3);

        Customer duke = Customer.builder()
                .customerName("Duke Barrow")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer jake = Customer.builder()
                .customerName("Jake Lambert")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer mike = Customer.builder()
                .customerName("Mike Mall")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerRepo.save(duke);
        customerRepo.save(jake);
        customerRepo.save(mike);

        log.debug("Ending Bootstrap Data...");

    }
}
