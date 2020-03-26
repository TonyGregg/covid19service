package com.learning.couchbase.covid19service.model;

import lombok.Data;

@Data
public class Counter {
    private String localDate;
    private int count;
}