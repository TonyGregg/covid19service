package com.learning.couchbase.covid19service.services;

import com.learning.couchbase.covid19service.model.LocationStat;

import java.io.IOException;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:21 PM
 * For project : covid19service
 **/
public interface CoronavirusService {
    public static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    public List<LocationStat> fetchVirusData() throws IOException, InterruptedException;
}
