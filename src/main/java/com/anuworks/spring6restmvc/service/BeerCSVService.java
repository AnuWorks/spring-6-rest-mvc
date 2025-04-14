package com.anuworks.spring6restmvc.service;

import com.anuworks.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCSVService {
    List<BeerCSVRecord> convertCSV(File file);
}
