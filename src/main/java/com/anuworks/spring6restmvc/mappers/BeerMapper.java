package com.anuworks.spring6restmvc.mappers;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);
    BeerDTO beerToBeerDTO(Beer beer);
}
