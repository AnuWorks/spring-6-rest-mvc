package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getListOfBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO createNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerByID(UUID beerId, BeerDTO beer);

    Boolean deleteBeerByID(UUID beerId);

    Optional<BeerDTO> patchBeerByID(UUID beerId, BeerDTO beer);
}
