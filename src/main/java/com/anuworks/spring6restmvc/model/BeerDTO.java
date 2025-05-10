package com.anuworks.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BeerDTO {

    private UUID id;
    private Integer version;
    @NotBlank
    @NotNull
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;
    @NotBlank
    @NotNull
    private String upc;

    @NotNull
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;



}
