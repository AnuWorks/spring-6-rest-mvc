package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Beer;
import com.anuworks.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.LAGER)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.GINGER_BEER)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get beer by id in service: {}", beerMap.get(id));
        return beerMap.get(id);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {

        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);
        log.debug("New Beer ID:: {}", savedBeer.getId());
        return savedBeer;
    }

    @Override
    public void updateBeerByID(UUID beerId, Beer beer) {
        log.debug("Update beer by id in service: {}", beerId);

        Beer existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setBeerStyle(beer.getBeerStyle());
        existing.setUpc(beer.getUpc());
        existing.setPrice(beer.getPrice());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        existing.setLastUpdatedDate(LocalDateTime.now());
        beerMap.put(existing.getId(), existing);


    }

    @Override
    public void deleteBeerByID(UUID beerId) {
        log.debug("Delete beer by id in service: {}", beerId);
        beerMap.remove(beerId);
    }

    @Override
    public Beer patchBeerByID(UUID beerId, Beer beer) {
        log.debug("Patch beer by id in service: {}", beerId);

        Beer existing = beerMap.get(beerId);
        // Check and update
        if(StringUtils.hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
        }
        if(StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }
        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }
        if(beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }
        existing.setLastUpdatedDate(LocalDateTime.now());
        beerMap.put(beerId, existing);
        return existing;

    }
}
