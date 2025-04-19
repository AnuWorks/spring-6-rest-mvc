package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.model.BeerStyle;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BeerRepo extends JpaRepository<Beer, UUID> {
    List<Beer> findByBeerNameIsLikeIgnoreCase(String name);

    List<Beer> findByBeerStyle(@NotNull BeerStyle beerStyle);

    List<Beer> findByBeerNameIsLikeIgnoreCaseAndBeerStyle(@NotNull String beerName,
                                                          @NotNull BeerStyle beerStyle);


}
