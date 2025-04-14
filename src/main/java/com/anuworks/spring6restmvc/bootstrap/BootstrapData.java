package com.anuworks.spring6restmvc.bootstrap;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.entities.Customer;
import com.anuworks.spring6restmvc.model.BeerCSVRecord;
import com.anuworks.spring6restmvc.model.BeerStyle;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import com.anuworks.spring6restmvc.repo.CustomerRepo;
import com.anuworks.spring6restmvc.service.BeerCSVService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final BeerRepo beerRepo;
    private final CustomerRepo customerRepo;
    private final BeerCSVService beerCSVService;

    @Override
    public void run(String... args) throws Exception {
        log.debug("Starting Bootstrap Data...");
        loadBeers();
        loadCustomer();
        loadCSVData();
        log.debug("Ending Bootstrap Data...");

    }

    private void loadCSVData() throws FileNotFoundException {
        if(beerRepo.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
            List<BeerCSVRecord> beerList = beerCSVService.convertCSV(file);

            beerList.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepo.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }


    // Methods to load data
    private void loadCustomer() {
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
    }

    private void loadBeers() {
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
                .beerStyle(BeerStyle.PILSNER)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        beerRepo.save(beer1);
        beerRepo.save(beer2);
        beerRepo.save(beer3);
    }
}
