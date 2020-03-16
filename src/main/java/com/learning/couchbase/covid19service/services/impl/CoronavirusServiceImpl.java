package com.learning.couchbase.covid19service.services.impl;

import com.learning.couchbase.covid19service.model.LocationStat;
import com.learning.couchbase.covid19service.services.CoronavirusService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:22 PM
 * For project : covid19service
 **/
@Service
@Slf4j
public class CoronavirusServiceImpl implements CoronavirusService {
    private List<LocationStat> allStats = new ArrayList<>();
    @Override
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
//    @Scheduled(cron = "5 * * * * *")
    public List<LocationStat> fetchVirusData() throws IOException, InterruptedException {
        List<LocationStat> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//        log.info("Response from the GIT Covid 19 CSSE URL :: "+httpResponse.body());

        StringReader csvStringReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvStringReader);
        LocationStat locationStat;
        for (CSVRecord record : records) {
            locationStat = new LocationStat();
            String state = record.get("Province/State");
            locationStat.setState(state);
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            locationStat.setNumberOfNewCasesToday(Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2)));

            log.info(locationStat.toString());
            newStats.add(locationStat);
//            String customerNo = record.get("CustomerNo");
//            String name = record.get("Name");
        }
        this.allStats = newStats;
        return newStats;
    }
}
