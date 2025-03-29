package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepo beerRepo;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> getListOfBeers() {
        return beerRepo.findAll()
                .stream()
                .map(beerMapper::beerToBeerDTO)
                .toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDTO(
                        beerRepo.findById(id).orElse(null)
        ));
    }

    @Override
    public BeerDTO createNewBeer(BeerDTO beer) {
        return null;
    }

    @Override
    public void updateBeerByID(UUID beerId, BeerDTO beer) {

    }

    @Override
    public void deleteBeerByID(UUID beerId) {

    }

    @Override
    public BeerDTO patchBeerByID(UUID beerId, BeerDTO beer) {
        return null;
    }
}
