package com.anuworks.spring6restmvc.controller;

import com.anuworks.spring6restmvc.model.Beer;
import com.anuworks.spring6restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    public Beer getBeerById(@PathVariable("beerId") String beerId) {
        log.debug("Get beer by id in controller: {}", beerId);
        UUID id = UUID.fromString(beerId);
        return beerService.getBeerById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> getAllBeers() {
        log.debug("Get all beers");
        return beerService.listBeers();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer){
        log.debug("Handle post beer: {}", beer);
        Beer saveNewBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + saveNewBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<HttpStatus> updateByID(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
        log.debug("Update beer by id in controller: {}", beerId);

        beerService.updateBeerByID(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
