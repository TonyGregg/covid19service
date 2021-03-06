package com.learning.couchbase.covid19service.services.impl;

import com.learning.couchbase.covid19service.model.*;
import com.learning.couchbase.covid19service.services.DailyTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
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
    Tracker usTracker;
    Tracker indiaTracker;

    @PostConstruct
    @Scheduled(cron = "0 */10 * * * *") // Runs at every 10th minute
    public void initializeData() {
        log.info("Loading data... "+LocalDateTime.now());
        usTracker = initializeData(DailyTrackerService.CONSOLIDATED_TRACKER_US);
        indiaTracker = initializeData(DailyTrackerService.CONSOLIDATED_TRACKER_INDIA);
        log.info("Finished loading data... "+LocalDateTime.now());
    }

    private Tracker initializeData(String URL) {
        log.info("URL used.."+URL);
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
        ArrayList<Label> labels = new ArrayList<>();
        ArrayList<Value> cumulativeCases = new ArrayList<>();
        ArrayList<Value> newCases = new ArrayList<>();
        Value cumulativeValue = null;
        Value newValue = null;
        Label label = null;
        CSVRecord currentRec = null;
        try {

            for (CSVRecord record : records) {
                currentRec =  record;
                label = new Label(record.get(0)); // Date
                labels.add(label);
                cumulativeValue = new Value(Integer.parseInt(record.get(1))); // cumulative count
                newValue = new Value(Integer.parseInt(record.get(2))); // New count
                cumulativeCases.add(cumulativeValue);
                newCases.add(newValue);
            }
        } catch (NumberFormatException ex) {
            log.error("Number format exception "+ex + " Record " + currentRec);
        }
        tracker.setCumulativeValues(cumulativeCases);
        tracker.setNewValues(newCases);
        tracker.setLabels(labels);

        return tracker;
    }

    @Override
    public Tracker getTracker(String country) {
        Tracker tracker = null;
        switch (country) {
            case "US":
            case "us":
                tracker = this.usTracker;
                break;
            case "IN":
            case "in":
            case "India":
                tracker = this.indiaTracker;
                break;
        }
        return tracker;

    }
}
