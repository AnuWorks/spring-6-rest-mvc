package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Beer;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<Beer> getListOfBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer createNewBeer(Beer beer);

    void updateBeerByID(UUID beerId, Beer beer);

    void deleteBeerByID(UUID beerId);

    Beer patchBeerByID(UUID beerId, Beer beer);
}
