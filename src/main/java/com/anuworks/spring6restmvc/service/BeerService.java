package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Page<BeerDTO> getListOfBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO createNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerByID(UUID beerId, BeerDTO beer);

    Boolean deleteBeerByID(UUID beerId);

    Optional<BeerDTO> patchBeerByID(UUID beerId, BeerDTO beer);
}
