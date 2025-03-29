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
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class BeerController {

    private static final String BEER_PATH = "/api/v1/beer";
    private static final String BEER_PATH_ID = "/api/v1/beer/{beerId}";

    private final BeerService beerService;

    @GetMapping(value = BEER_PATH_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get beer by id in controller: {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_PATH)
    public List<Beer> getAllBeers() {
        log.debug("Get all beers");
        return beerService.getListOfBeers();
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer){
        log.debug("Handle post beer: {}", beer);
        Beer saveNewBeer = beerService.createNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + saveNewBeer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<HttpStatus> updateByID(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
        log.debug("Update beer by id in controller: {}", beerId);

        beerService.updateBeerByID(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<HttpStatus> deleteByID(@PathVariable("beerId") UUID beerId){
        log.debug("Delete beer by id in controller: {}", beerId);
        beerService.deleteBeerByID(beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> patchByID(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer){
        log.debug("Patch beer by id in controller: {}", beerId);
        Beer updatedBeer = beerService.patchBeerByID(beerId, beer);
        return new ResponseEntity<>(updatedBeer, HttpStatus.NO_CONTENT);
    }

}
