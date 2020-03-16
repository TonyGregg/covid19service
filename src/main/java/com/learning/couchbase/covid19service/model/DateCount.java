package com.learning.couchbase.covid19service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Created by Antony Genil Gregory on 3/16/2020 5:00 AM
 * For project : covid19service
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateCount {
    private LocalDate date;
    private int count;
}
