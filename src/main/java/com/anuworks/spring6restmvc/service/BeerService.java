package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
