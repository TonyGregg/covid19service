package com.learning.couchbase.covid19service.services.impl;

import com.learning.couchbase.covid19service.model.DateCount;
import com.learning.couchbase.covid19service.model.VirusFastDashBoard;
import com.learning.couchbase.covid19service.model.VirusStatDataFastHolder;
import com.learning.couchbase.covid19service.model.VirusStatDataHolder;
import com.learning.couchbase.covid19service.services.CoronavirusFastService;
import com.learning.couchbase.covid19service.services.CoronavirusService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:22 PM
 * For project : covid19service
 **/
@Service
@Slf4j
public class CoronavirusFastServiceImpl implements CoronavirusFastService {


    private VirusFastDashBoard virusConfirmedDashBoard = new VirusFastDashBoard();
    private VirusFastDashBoard virusRecoveredDashBoard = new VirusFastDashBoard();
    private VirusFastDashBoard virusDeathDashBoard = new VirusFastDashBoard();


    @Override
    public VirusFastDashBoard getConfirmedCases(){
        return this.virusConfirmedDashBoard;
    }

    @Override
    public VirusFastDashBoard getRecoveredCases() {
        return this.virusRecoveredDashBoard;
    }

    @Override
    public VirusFastDashBoard getDeathCases() {
        return this.virusDeathDashBoard;
    }

    @PostConstruct
    @Scheduled(cron = "0 */32 * * * *") // Runs at every 30th minute
    private void fetchVirusData() throws IOException, InterruptedException {
        log.info("Updating confirmed case records..");
        VirusFastDashBoard confirmedVirusDb = fetchVirusData(COVID19_CONFIRMED_CASES_URL);
        this.virusConfirmedDashBoard = confirmedVirusDb;
        log.info("Confirmed case records updated");

        log.info("Updating recovered cases.. ");
        VirusFastDashBoard recoveredDb = fetchVirusData(COVID19_RECOVERED_CASES_URL);
        this.virusRecoveredDashBoard = recoveredDb;
        log.info("Updated Recovered Cases ");

        log.info("Updating death cases.. ");
        VirusFastDashBoard  deathDb = fetchVirusData(COVID19_DEATH_CASES_URL);
        this.virusDeathDashBoard = deathDb;
        log.info("Updated death Cases :: ");


    }

    private VirusFastDashBoard fetchVirusData(String URL) throws IOException, InterruptedException{
        VirusFastDashBoard virusFastDashBoard = new VirusFastDashBoard();
        log.info("Fetching records from "+URL);
        List<VirusStatDataFastHolder> virusStatDataHolderList = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//        log.info(httpResponse.body());

        StringReader csvStringReader = new StringReader(httpResponse.body());
        StringReader csvStringReader1 = new StringReader(httpResponse.body());


        List<String> headerNames = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvStringReader1).getHeaderNames();

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvStringReader);

        log.info("# of headers  "+headerNames.size());



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yy");
//        if (COVID19_RECOVERED_CASES_URL.equals(URL)) {
//            formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");
//        }
        String lastDataAvailable = headerNames.get(headerNames.size() - 1); // last column is the latest date
        virusFastDashBoard.setLatestReportDate(LocalDate.parse(lastDataAvailable,formatter));
        virusFastDashBoard.setRecordLastUpdated(LocalDateTime.now());


        VirusStatDataFastHolder virusStatDataHolder;
        CSVRecord currentRec = null;
        try {
            for (CSVRecord record : records) {
                currentRec = record;
                virusStatDataHolder = new VirusStatDataFastHolder();
                virusStatDataHolder.setState(record.get(0)); // Province/State
                virusStatDataHolder.setCountry(record.get(1)); // Country/Region
//            log.info(record.toString());

                virusStatDataHolder.setLatitude(Double.parseDouble(record.get(2))); // Latitude
                virusStatDataHolder.setLongitude(Double.parseDouble(record.get(3))); // Longitude
                virusStatDataHolder.setTotalCases(Integer.parseInt(record.get(record.size() - 1))); // Latest total cases
                virusStatDataHolder.setNewCaseCount(Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2)));



                virusStatDataHolderList.add(virusStatDataHolder);
            }
        } catch (NumberFormatException ex) {
            log.error("Number format exception "+ex + " Record " + currentRec);
        }

        virusFastDashBoard.setVirusStatDataHolderList(virusStatDataHolderList);
        return virusFastDashBoard;

    }


}
