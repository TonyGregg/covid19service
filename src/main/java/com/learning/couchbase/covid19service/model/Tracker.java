package com.learning.couchbase.covid19service.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Antony Genil on 3/26/20 at 07 35 for covid19service
 **/
@Data
public class Tracker {
    private List<Label> labels;
    private List<Value> cumulativeValues;
    private List<Value> newValues;
}

