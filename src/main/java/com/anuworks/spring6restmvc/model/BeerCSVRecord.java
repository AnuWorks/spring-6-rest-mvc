package com.anuworks.spring6restmvc.model;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Anudeep Madrampalli (Anuworks)
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerCSVRecord {

    @CsvBindByName
    private Integer row;
    @CsvBindByName(column = "count.x")
    private Integer count;
    @CsvBindByName
    private String abv;
    @CsvBindByName
    private String ibu;
    @CsvBindByName
    private Integer id;
    @CsvBindByName
    private String beer;
    @CsvBindByName
    private String style;
    @CsvBindByName
    private Integer breweryId;
    @CsvBindByName
    private Float ounces;
    @CsvBindByName
    private String style2;
    @CsvBindByName(column = "count.y")
    private String count_y;
    @CsvBindByName(column = "brewery_id")
    private String brewery;
    @CsvBindByName
    private String city;
    @CsvBindByName
    private String state;
    @CsvBindByName
    private String label;





}
