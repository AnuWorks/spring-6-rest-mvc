package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> getListOfBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO createNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerByID(UUID beerId, BeerDTO beer);

    void deleteBeerByID(UUID beerId);

    BeerDTO patchBeerByID(UUID beerId, BeerDTO beer);
}
