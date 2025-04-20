package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.mappers.BeerMapper;
import com.anuworks.spring6restmvc.model.BeerDTO;
import com.anuworks.spring6restmvc.model.BeerStyle;
import com.anuworks.spring6restmvc.repo.BeerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final static Integer DEFAULT_PAGE_OFFSET = 0;
    private final static Integer DEFAULT_PAGE_SIZE = 25;


    @Override
    public Page<BeerDTO> getListOfBeers(String beerName,
                                        BeerStyle beerStyle,
                                        Boolean showInventory,
                                        Integer pageNumber,
                                        Integer pageSize) {
        log.debug("Get list of beers from database");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage = null;
        if(StringUtils.hasText(beerName)){
            beerPage = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle!= null) {
            beerPage =  listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle!= null) {
            beerPage = listBeertsByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepo.findAll(pageRequest);
        }

        if(showInventory != null && !showInventory){
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDTO);

    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        }else {
            queryPageNumber = DEFAULT_PAGE_OFFSET;
        }
        if(pageSize == null){
            queryPageSize = DEFAULT_PAGE_SIZE;
        }else {
            if(pageSize > 1000){
                pageSize = 1000;
            }
            queryPageSize = pageSize;
        }

        Sort sort = Sort.by(Sort.Order.by("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);

    }

    private Page<Beer> listBeertsByNameAndStyle(String beerName, BeerStyle beerStyle, PageRequest pageRequest) {
        return beerRepo.findByBeerNameIsLikeIgnoreCaseAndBeerStyle(beerName, beerStyle, null);
    }

    Page<Beer> listBeersByName(String name, PageRequest pageRequest) {
        log.debug("Get list of beers from database by name: {}", name);
        return beerRepo.findByBeerNameIsLikeIgnoreCase(name, null);
    }

    Page<Beer> listBeersByStyle(BeerStyle style, PageRequest pageRequest) {
        log.debug("Get list of beers from database by style: {}", style);
        return beerRepo.findByBeerStyle(style, null);
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
