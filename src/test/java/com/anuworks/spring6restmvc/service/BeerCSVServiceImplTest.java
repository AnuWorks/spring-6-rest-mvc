package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerCSVServiceImplTest {

    @Autowired
    private BeerCSVService beerCSVService;

    @Test
    void name() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCSVRecord> beerCSVRecordList = beerCSVService.convertCSV(file);
        System.out.println(beerCSVRecordList.size());
        assertThat(beerCSVRecordList.size()).isGreaterThan(0);


    }
}