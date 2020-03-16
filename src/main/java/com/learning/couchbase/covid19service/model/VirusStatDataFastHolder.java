package com.learning.couchbase.covid19service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 7:53 PM
 * For project : covid19service
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VirusStatDataFastHolder {
    private String state;
    private String country;
    private int totalCases;
    private double latitude;
    private double longitude;
    private int newCaseCount;

}
