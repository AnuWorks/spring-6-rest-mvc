package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.BeerCSVRecord;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Service
@Slf4j
public class BeerCSVServiceImpl implements BeerCSVService {
    @Override
    public List<BeerCSVRecord> convertCSV(File file) {
        log.info("Converting csv file: {}", file.getAbsolutePath());
        List<BeerCSVRecord> beerCSVRecordList;
        try {
            beerCSVRecordList = new CsvToBeanBuilder<BeerCSVRecord>(new CSVReader(new FileReader(file)))
                    .withType(BeerCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return beerCSVRecordList;
    }
}
