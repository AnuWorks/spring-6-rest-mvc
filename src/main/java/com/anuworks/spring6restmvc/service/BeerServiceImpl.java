package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Beer;
import com.anuworks.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get beer by id in service: {}", id);
        return Beer.builder()
                .id(id)
                .version(1)
                .beerName("Northern Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("123456789")
                .price(new BigDecimal("12.23"))
                .createdDate(LocalDate.now())
                .lastUpdatedDate(LocalDate.now())
                .build();
    }
}
