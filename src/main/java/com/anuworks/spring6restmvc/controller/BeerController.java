package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Beer;
import com.anuworks.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get beer by id in controller: {}", beerId);
        return beerService.getBeerById(beerId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> getAllBeers() {
        log.debug("Get all beers");
        return beerService.listBeers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer){
        log.debug("Handle post beer: {}", beer);
        Beer beer1 = beerService.saveNewBeer(beer);
        return new ResponseEntity<>(beer1, HttpStatus.CREATED);
    }


}
