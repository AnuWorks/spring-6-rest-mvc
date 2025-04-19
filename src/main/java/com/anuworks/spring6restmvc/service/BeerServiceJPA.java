package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.model.BeerStyle;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class BeerServiceJPA implements BeerService {

    private final BeerRepo beerRepo;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> getListOfBeers(String beerName, BeerStyle beerStyle, Boolean showInventory) {
        log.debug("Get list of beers from database");

        List<Beer> beerList = null;
        if(StringUtils.hasText(beerName)){
            beerList = listBeersByName(beerName);
        } else if (!StringUtils.hasText(beerName) && beerStyle!= null) {
            beerList =  listBeersByStyle(beerStyle);
        } else if (StringUtils.hasText(beerName) && beerStyle!= null) {
            beerList = listBeertsByNameAndStyle(beerName, beerStyle);
        } else {
            beerList = beerRepo.findAll();
        }

        if(showInventory != null && !showInventory){
            beerList.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerList.stream()
                .map(beerMapper::beerToBeerDTO)
                .toList();
    }

    private List<Beer> listBeertsByNameAndStyle(String beerName, BeerStyle beerStyle) {
        return beerRepo.findByBeerNameIsLikeIgnoreCaseAndBeerStyle(beerName, beerStyle);
    }

    List<Beer> listBeersByName(String name) {
        log.debug("Get list of beers from database by name: {}", name);
        return beerRepo.findByBeerNameIsLikeIgnoreCase(name);
    }

    List<Beer> listBeersByStyle(BeerStyle style) {
        log.debug("Get list of beers from database by style: {}", style);
        return beerRepo.findByBeerStyle(style);
    }


    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDTO(
                        beerRepo.findById(id).orElse(null)
        ));
    }

    @Override
    public BeerDTO createNewBeer(BeerDTO beer) {
        log.debug("createNewBeer service JPA: {}", beer);
        return beerMapper.beerToBeerDTO(beerRepo.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updateBeerByID(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> existingBeer = new AtomicReference<>();

        beerRepo.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setLastUpdatedDate(LocalDateTime.now());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            existingBeer.set(Optional.of(beerMapper.beerToBeerDTO(beerRepo.save(foundBeer))));

        }, () -> {
            existingBeer.set(Optional.empty());
        });
        return existingBeer.get();
    }

    @Override
    public Boolean deleteBeerByID(UUID beerId) {
        if(beerRepo.existsById(beerId)) {
            beerRepo.deleteById(beerId);
            return true;
        }
        return false;

    }

    @Override
    public Optional<BeerDTO> patchBeerByID(UUID beerId, BeerDTO beer) {
        log.debug("Patch beer by id in service: {}", beerId);
        AtomicReference<Optional<BeerDTO>> beerRef = new AtomicReference<>();

        beerRepo.findById(beerId).ifPresentOrElse(
                foundBeer -> {
                    foundBeer.setBeerName(beer.getBeerName());
                    foundBeer.setBeerStyle(beer.getBeerStyle());
                    foundBeer.setPrice(beer.getPrice());
                    foundBeer.setLastUpdatedDate(LocalDateTime.now());
                    foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    foundBeer.setLastUpdatedDate(LocalDateTime.now());
                    beerRepo.save(foundBeer);
                    beerRef.set(Optional.of(beerMapper.beerToBeerDTO(foundBeer)));
                }, () -> {
                    beerRef.set(Optional.empty());
                });
        return beerRef.get();


    }
}
