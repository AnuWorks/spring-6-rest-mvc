package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Beer;
import com.anuworks.spring6restmvc.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepoTest {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    BeerRepo beerRepo;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepo.findAll().get(0);
    }

    @Test
    void testCategoryRepo() {
        Category category = Category.builder().description("Ales").build();
        Category savedCategory = categoryRepo.save(category);

    }
}