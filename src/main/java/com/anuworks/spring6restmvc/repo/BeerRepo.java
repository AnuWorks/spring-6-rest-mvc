package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerRepo extends JpaRepository<Beer, UUID> {
}
