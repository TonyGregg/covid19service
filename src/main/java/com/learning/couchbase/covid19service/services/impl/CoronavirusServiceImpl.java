package com.learning.couchbase.covid19service.services.impl;

import com.learning.couchbase.covid19service.model.VirusStatDataHolder;
import com.learning.couchbase.covid19service.model.DateCount;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:22 PM
 * For project : covid19service
 **/
@Service
@Slf4j
public class CoronavirusServiceImpl implements CoronavirusService {


    private List<VirusStatDataHolder> virusConfirmedList = new ArrayList<>();
    private List<VirusStatDataHolder> virusRecoveredList = new ArrayList<>();
    private List<VirusStatDataHolder> virusDeathList = new ArrayList<>();

    @Override
    public List<VirusStatDataHolder> getConfirmedCases(){
        return virusConfirmedList;
    }

    @Override
    public List<VirusStatDataHolder> getRecoveredCases() {
        return virusRecoveredList;
    }

    @Override
    public List<VirusStatDataHolder> getDeathCases() {
        return virusDeathList;
    }

    @PostConstruct
    @Scheduled(cron = "0 */30 * * * *") // Runs at every 30th minute
    private void fetchVirusData() throws IOException, InterruptedException {
        log.info("Updating confirmed case records..");
        List<VirusStatDataHolder> confirmedList = fetchVirusData(COVID19_CONFIRMED_CASES_URL);
        this.virusConfirmedList = confirmedList;
        log.info("Confirmed case records updated");

        log.info("Updating recovered cases.. ");
        List<VirusStatDataHolder> recoveredList = fetchVirusData(COVID19_RECOVERED_CASES_URL);
        this.virusRecoveredList = recoveredList;
        log.info("Updated Recovered Cases ");

        log.info("Updating death cases.. ");
        List<VirusStatDataHolder> deathList = fetchVirusData(COVID19_DEATH_CASES_URL);
        this.virusDeathList = deathList;
        log.info("Updated death Cases ");


    }

    private List<VirusStatDataHolder> fetchVirusData(String URL) throws IOException, InterruptedException{
        log.info("Fetching records from "+URL);
        List<VirusStatDataHolder> virusStatDataHolderList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvStringReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvStringReader);
        VirusStatDataHolder virusStatDataHolder;
        for (CSVRecord record : records) {
            virusStatDataHolder = new VirusStatDataHolder();
            virusStatDataHolder.setState(record.get(0)); // Province/State
            virusStatDataHolder.setCountry(record.get(1)); // Country/Region
            virusStatDataHolder.setLatitude(Double.parseDouble(record.get(2))); // Latitude
            virusStatDataHolder.setLongitude(Double.parseDouble(record.get(3))); // Longitude
            virusStatDataHolder.setTotalCases(Integer.parseInt(record.get(record.size() - 1))); // Latest total cases
            virusStatDataHolder.setNewCaseCount(Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2)));
            virusStatDataHolder.setDateCountList(getDateCount(record));
            virusStatDataHolder.setRecordLastUpdated(LocalDateTime.now());
            virusStatDataHolderList.add(virusStatDataHolder);
        }
        return virusStatDataHolderList;

    }

    private List<DateCount> getDateCount(CSVRecord record) {
        List<DateCount> dateCountList = new ArrayList<>();
        // Start with record column # 4 (1st case data available Jan 22, 2020) to all the way to size - 1
        DateCount dateCount = null;
        int col = 4;
        LocalDate localDate = LocalDate.of(2020, Month.JANUARY, 22);
        while (col < record.size()) {
            dateCount = new DateCount();
            dateCount.setDate(localDate);
            dateCount.setCount(Integer.parseInt(record.get(col)));
            localDate = localDate.plusDays(1);
            col++;
            dateCountList.add(dateCount);
        }

        return dateCountList;
    }
}
