package com.learning.couchbase.covid19service.services;

import com.learning.couchbase.covid19service.model.Tracker;

/**
 * Created by Antony Genil Gregory on 3/26/2020 12:06 PM
 * For project : covid19service
 **/
public interface DailyTrackerService {
    public static String CONSOLIDATED_TRACKER_US = "https://raw.githubusercontent.com/TonyGregg/covid19data/master/data/" +
            "US.csv";
    public static String CONSOLIDATED_TRACKER_INDIA = "https://raw.githubusercontent.com/TonyGregg/covid19data/master/" +
            "data/India.csv";
    public Tracker getTracker(String country);
}
