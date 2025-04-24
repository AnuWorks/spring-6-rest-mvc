package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerOrderRepo extends JpaRepository<BeerOrder, UUID> {
}
