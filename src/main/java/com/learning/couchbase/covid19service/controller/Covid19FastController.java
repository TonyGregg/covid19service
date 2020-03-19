package com.learning.couchbase.covid19service.controller;

import com.learning.couchbase.covid19service.model.VirusDashBoard;
import com.learning.couchbase.covid19service.model.VirusFastDashBoard;
import com.learning.couchbase.covid19service.model.VirusStatDataFastHolder;
import com.learning.couchbase.covid19service.model.VirusStatDataHolder;
import com.learning.couchbase.covid19service.services.CoronavirusFastService;
import com.learning.couchbase.covid19service.services.CoronavirusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:31 PM
 * For project : covid19service
 **/
@RestController
@RequestMapping("/api/v2")
@Slf4j
public class Covid19FastController {
    @Autowired
    CoronavirusFastService coronavirusService;
    @RequestMapping("/confirmed")
    public VirusFastDashBoard getAllConfirmedCases() {
        log.info("Getting confirmed cases across the globe");

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getConfirmedCases();
        List<VirusStatDataFastHolder> sortedList =
        virusFastDashBoard.getVirusStatDataHolderList().stream()
                .sorted(Comparator.comparing(VirusStatDataFastHolder::getTotalCases).reversed())
                .collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);

        updateTotalCounts(virusFastDashBoard);

        return virusFastDashBoard;
    }


    @RequestMapping("/recovered")
    public VirusFastDashBoard getAllRecovered() {
        log.info("Getting recovered cases across the globe");

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getRecoveredCases();
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparingInt(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);

        updateTotalCounts(virusFastDashBoard);
        return virusFastDashBoard;
    }

    @RequestMapping("/death")
    public VirusFastDashBoard getAllDeath() {
        log.info("Getting recovered cases across the globe");

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getDeathCases();
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparingInt(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);

        updateTotalCounts(virusFastDashBoard);
        return virusFastDashBoard;
    }


    private void updateTotalCounts(VirusFastDashBoard virusFastDashBoard) {
        int totalCount;
        int totalNewCount;
        totalCount = virusFastDashBoard.
                getVirusStatDataHolderList().stream().mapToInt(virusStatHolder -> virusStatHolder.getTotalCases()).sum();
        totalNewCount = virusFastDashBoard.
                getVirusStatDataHolderList().stream().mapToInt(virusStatHolder -> virusStatHolder.getNewCaseCount()).sum();

        virusFastDashBoard.setTotalReported(totalCount);
        virusFastDashBoard.setTotalNew(totalNewCount);
    }



    // Filtered cases

    @RequestMapping("/confirmed/{country}")
    public VirusFastDashBoard getConfirmedCountry(@PathVariable("country") @NotNull @NotEmpty String country) {
        log.info("Getting confirmed cases for a specific country : "+country);

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getConfirmedCases();
        VirusFastDashBoard filteredBoard = filterVirusDashBoard(virusFastDashBoard, country);
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparing(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);

        updateTotalCounts(filteredBoard);
        filteredBoard.setRecordLastUpdated(virusFastDashBoard.getRecordLastUpdated());
        filteredBoard.setLatestReportDate(virusFastDashBoard.getLatestReportDate());
        return filteredBoard;
    }
    @RequestMapping("/confirmed/{country}/{state}")
    public VirusFastDashBoard getConfirmedCountryState(@PathVariable("country") @NotNull @NotEmpty String country,
                                                   @PathVariable("state") @NotNull @NotEmpty String state) {
        log.info("Getting confirmed cases across  for country : "+country + " and state : "+state);

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getConfirmedCases();
        VirusFastDashBoard filteredBoard = filterVirusDashBoard(virusFastDashBoard, country, state);
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparing(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);

        updateTotalCounts(filteredBoard);
        filteredBoard.setRecordLastUpdated(virusFastDashBoard.getRecordLastUpdated());
        filteredBoard.setLatestReportDate(virusFastDashBoard.getLatestReportDate());
        return filteredBoard;
    }

    @RequestMapping("/recovered/{country}")
    public VirusFastDashBoard getRecoveredCountry(@PathVariable("country") @NotNull @NotEmpty String country) {
        log.info("Getting recovered cases for a country : "+country);

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getRecoveredCases();
        VirusFastDashBoard filteredBoard = filterVirusDashBoard(virusFastDashBoard, country);
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparingInt(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);
        updateTotalCounts(filteredBoard);
        filteredBoard.setRecordLastUpdated(virusFastDashBoard.getRecordLastUpdated());
        filteredBoard.setLatestReportDate(virusFastDashBoard.getLatestReportDate());
        return filteredBoard;
    }
    @RequestMapping("/recovered/{country}/{state}")
    public VirusFastDashBoard getRecoveredCountryState(@PathVariable("country") @NotNull @NotEmpty String country,
                                                   @PathVariable("state") @NotNull @NotEmpty String state) {
        log.info("Getting recovered cases across  for country : "+country + " and state "+state);

        VirusFastDashBoard virusFastDashBoard = coronavirusService.getRecoveredCases();
        VirusFastDashBoard filteredBoard = filterVirusDashBoard(virusFastDashBoard, country, state);
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparingInt(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);
        updateTotalCounts(filteredBoard);
        filteredBoard.setRecordLastUpdated(virusFastDashBoard.getRecordLastUpdated());
        filteredBoard.setLatestReportDate(virusFastDashBoard.getLatestReportDate());
        return filteredBoard;
    }


    @RequestMapping("/death/{country}")
    public VirusFastDashBoard getDeathCountry(@PathVariable("country") @NotNull @NotEmpty String country) {
        log.info("Getting death cases across  for country : "+country);
        VirusFastDashBoard virusFastDashBoard = coronavirusService.getDeathCases();
        VirusFastDashBoard filteredBoard = filterVirusDashBoard(virusFastDashBoard, country);
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparingInt(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);
        updateTotalCounts(filteredBoard);
        filteredBoard.setRecordLastUpdated(virusFastDashBoard.getRecordLastUpdated());
        filteredBoard.setLatestReportDate(virusFastDashBoard.getLatestReportDate());
        return filteredBoard;
    }
    @RequestMapping("/death/{country}/{state}")
    public VirusFastDashBoard getDeathCountryState(@PathVariable("country") @NotNull @NotEmpty String country,
                                               @PathVariable("state") @NotNull @NotEmpty String state) {
        log.info("Getting death cases across  for country : "+country + " and state : "+state);
        VirusFastDashBoard virusFastDashBoard = coronavirusService.getDeathCases();
        VirusFastDashBoard filteredVirusBoard =  filterVirusDashBoard(virusFastDashBoard, country, state);
        List<VirusStatDataFastHolder> sortedList =
                virusFastDashBoard.getVirusStatDataHolderList().stream().
                        sorted(Comparator.comparingInt(VirusStatDataFastHolder::getTotalCases).reversed()).
                        collect(Collectors.toList());
        virusFastDashBoard.setVirusStatDataHolderList(sortedList);
        updateTotalCounts(filteredVirusBoard);
        filteredVirusBoard.setRecordLastUpdated(virusFastDashBoard.getRecordLastUpdated());
        filteredVirusBoard.setLatestReportDate(virusFastDashBoard.getLatestReportDate());
        return filteredVirusBoard;
    }

    @RequestMapping("/confirmed/countries")
    public List<String> getConfirmedCountryNames() {
        List<String> countries = coronavirusService.getConfirmedCases().getVirusStatDataHolderList()
                .stream().map(virusStatDataFastHolder -> virusStatDataFastHolder.getCountry())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return countries;
    }
    @RequestMapping("/confirmed/{country}/states")
    public Set<String> getConfirmedStateNames(@PathVariable("country") @NotBlank String country) {
        Set<String> states = coronavirusService.getConfirmedCases().getVirusStatDataHolderList()
                .stream().filter(viruStateHolder -> country.equalsIgnoreCase(viruStateHolder.getCountry()))
                .map(VirusStatDataFastHolder::getState).collect(Collectors.toSet());
        return states;
    }

    /**
     * Filter by country
     * @param virusFastDashBoard
     * @param country
     * @return
     */
    private VirusFastDashBoard filterVirusDashBoard(VirusFastDashBoard virusFastDashBoard, String country) {
        VirusFastDashBoard filteredVirusBoard = new VirusFastDashBoard();

        List<VirusStatDataFastHolder> filteredVirusData =
                virusFastDashBoard.getVirusStatDataHolderList().parallelStream().filter(virusStatDataHolder ->
                        country.equalsIgnoreCase(virusStatDataHolder.getCountry())).collect(Collectors.toList());
        filteredVirusBoard.setVirusStatDataHolderList(filteredVirusData);
        return filteredVirusBoard;

    }

    private VirusFastDashBoard filterVirusDashBoard(VirusFastDashBoard virusFastDashBoard, String country, String state) {
        VirusFastDashBoard filteredVirusBoard = new VirusFastDashBoard();
        List<VirusStatDataFastHolder> filteredVirusData =
                virusFastDashBoard.getVirusStatDataHolderList().parallelStream().filter(virusStatDataHolder ->
                        country.equalsIgnoreCase(virusStatDataHolder.getCountry())
                                && state.equalsIgnoreCase(virusStatDataHolder.getState())).collect(Collectors.toList());
        filteredVirusBoard.setVirusStatDataHolderList(filteredVirusData);

        return filteredVirusBoard;

    }
}
