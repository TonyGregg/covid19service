package com.learning.couchbase.covid19service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Antony Genil Gregory on 3/15/2020 7:53 PM
 * For project : covid19service
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationStat {
    private String state;
    private String country;
    private int latestTotalCases;
    private int numberOfNewCasesToday;

}
