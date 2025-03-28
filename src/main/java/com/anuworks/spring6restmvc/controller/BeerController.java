package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Beer;
import com.anuworks.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {
        log.debug("Get beer by id in controller: {}", id);
        return beerService.getBeerById(id);
    }


}
