package com.learning.couchbase.covid19service.services;

import com.learning.couchbase.covid19service.model.VirusFastDashBoard;
import com.learning.couchbase.covid19service.model.VirusStatDataFastHolder;
import com.learning.couchbase.covid19service.model.VirusStatDataHolder;

import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:21 PM
 * For project : covid19service
 **/
public interface CoronavirusFastService {

    // Confirmed cases CSV link
    public static String COVID19_CONFIRMED_CASES_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/" +
            "master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    // Recovered cases CSV link
    public static String COVID19_RECOVERED_CASES_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/" +
            "master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Recovered.csv";

    // Death cases
    public static String COVID19_DEATH_CASES_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master" +
            "/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Deaths.csv";

    public VirusFastDashBoard getConfirmedCases();
    public VirusFastDashBoard getRecoveredCases();
    public VirusFastDashBoard getDeathCases();


}
