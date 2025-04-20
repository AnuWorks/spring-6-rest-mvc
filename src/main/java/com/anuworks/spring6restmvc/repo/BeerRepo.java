package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.model.BeerStyle;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerRepo extends JpaRepository<Beer, UUID> {
    Page<Beer> findByBeerNameIsLikeIgnoreCase(String name, Pageable pageable);

    Page<Beer> findByBeerStyle(@NotNull BeerStyle beerStyle, Pageable pageable);

    Page<Beer> findByBeerNameIsLikeIgnoreCaseAndBeerStyle(@NotNull String beerName,
                                                          @NotNull BeerStyle beerStyle, Pageable pageable);


}
