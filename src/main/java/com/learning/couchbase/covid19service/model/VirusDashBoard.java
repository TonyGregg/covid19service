package com.learning.couchbase.covid19service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/16/2020 6:22 AM
 * For project : covid19service
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VirusDashBoard {
    private List<VirusStatDataHolder> virusStatDataHolderList;
    private int totalReported;
    private int totalNew;
    private LocalDate latestReportDate;
    private LocalDateTime recordLastUpdated;

}
