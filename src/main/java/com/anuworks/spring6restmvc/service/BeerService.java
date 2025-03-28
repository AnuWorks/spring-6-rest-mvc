package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> listBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerByID(UUID beerId, Beer beer);

    void deleteBeerByID(UUID beerId);

    Beer patchBeerByID(UUID beerId, Beer beer);
}
