package com.anuworks.spring6restmvc.model;

import com.anuworks.spring6restmvc.entities.BeerOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {

    private UUID id;
    private String customerName;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String customerEmail;
    private Set<BeerOrder> beerOrders;

}
