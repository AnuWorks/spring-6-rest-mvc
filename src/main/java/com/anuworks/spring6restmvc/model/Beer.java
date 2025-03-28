package com.anuworks.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class Beer {

    private UUID id;
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private BigDecimal price;
    private int quantityOnHand;
    private LocalDate createdDate;
    private LocalDate lastUpdatedDate;

}
