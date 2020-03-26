package com.learning.couchbase.covid19service.services.impl;

import com.learning.couchbase.covid19service.model.Counter;
import com.learning.couchbase.covid19service.model.Tracker;
import com.learning.couchbase.covid19service.model.VirusStatDataHolder;
import com.learning.couchbase.covid19service.services.DailyTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Antony Genil Gregory on 3/26/2020 12:34 PM
 * For project : covid19service
 **/
@Service
@Slf4j
public class DailyTrackerServiceImpl implements DailyTrackerService {

    @Override
    public Tracker getTracker(String country) {
        String URL = DailyTrackerService.CONSOLIDATED_TRACKER_US;;
        switch (country) {
            case "US":
            case "us":
                URL = DailyTrackerService.CONSOLIDATED_TRACKER_US;
                break;
            case "IN":
            case "in":
            case "India":
                URL = DailyTrackerService.CONSOLIDATED_TRACKER_US;
                break;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL)).build();
        HttpResponse<String> httpResponse = null;
        Iterable<CSVRecord> records = null;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            StringReader csvStringReader = new StringReader(httpResponse.body());

            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvStringReader);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Tracker tracker = new Tracker();
        ArrayList<Counter> cumulativeCases = new ArrayList<>();
        ArrayList<Counter> newCases = new ArrayList<>();
        Counter cumulativeCounter = null;
        Counter newCounter = null;
        CSVRecord currentRec = null;
        try {

            for (CSVRecord record : records) {
                currentRec =  record;
                cumulativeCounter = new Counter();
                newCounter = new Counter();

                cumulativeCounter.setLocalDate(record.get(0)); // Date
                cumulativeCounter. setCount(Integer.parseInt(record.get(1))); // cumulative count

                newCounter.setLocalDate(record.get(0)); // Date
                newCounter. setCount(Integer.parseInt(record.get(2))); // new count

                cumulativeCases.add(cumulativeCounter);
                newCases.add(newCounter);
            }
        } catch (NumberFormatException ex) {
            log.error("Number format exception "+ex + " Record " + currentRec);
        }
        tracker.setCumulativeCounterList(cumulativeCases);
        tracker.setNewCasesCounterList(newCases);

        return tracker;
    }
}
