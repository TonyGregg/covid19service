package com.learning.couchbase.covid19service.controller;

import com.learning.couchbase.covid19service.model.VirusDashBoard;
import com.learning.couchbase.covid19service.model.VirusStatDataHolder;
import com.learning.couchbase.covid19service.services.CoronavirusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:31 PM
 * For project : covid19service
 **/
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class Covid19Controller {
    @Autowired
    CoronavirusService coronavirusService;
    @RequestMapping("/confirmed")
    public VirusDashBoard getAllConfirmedCases() {
        List<VirusStatDataHolder> virusData = coronavirusService.getConfirmedCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData);
        return virusDashBoard;
    }

    @RequestMapping("/recovered")
    public VirusDashBoard getAllRecovered() {
        List<VirusStatDataHolder> virusData = coronavirusService.getRecoveredCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData);
        return virusDashBoard;
    }

    @RequestMapping("/death")
    public VirusDashBoard getAllDeath() {
        List<VirusStatDataHolder> virusData = coronavirusService.getDeathCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData);
        return virusDashBoard;
    }

    /**
     *
     * @param virusData list of stats
     * @return dashboard view with total confirmed cases so far and newly reported cases
     */
    private VirusDashBoard getVirusDashBoard(List<VirusStatDataHolder> virusData) {
        VirusDashBoard virusDashBoard = new VirusDashBoard();
        virusDashBoard.setVirusStatDataHolderList(virusData);
        virusDashBoard.setTotalReported(virusData.stream().
                mapToInt(virusStat -> virusStat.getTotalCases()).sum()); // Total confirmed cases from all regions
        virusDashBoard.setTotalNew(virusData.stream().
                mapToInt(data -> data.getNewCaseCount()).sum()); // Total # of new cases from all regions
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }
    private void updateLastRecordFetchDate(VirusDashBoard virusDashBoard) {
        if (virusDashBoard == null || virusDashBoard.getVirusStatDataHolderList() == null ||
                virusDashBoard.getVirusStatDataHolderList().size() < 1) return;

        int dateRecordCount = virusDashBoard.getVirusStatDataHolderList().get(0).getDateCountList().size();
        virusDashBoard.setLatestReportDate(virusDashBoard.getVirusStatDataHolderList().get(0).
                getDateCountList().get(dateRecordCount - 1).getDate());

        // Update last record fetch date and time
        virusDashBoard.setRecordLastUpdated(virusDashBoard.getVirusStatDataHolderList().get(0).getRecordLastUpdated());
    }

    // Filtered cases

    @RequestMapping("/confirmed/{country}")
    public VirusDashBoard getConfirmedCountry(@PathVariable("country") @NotNull @NotEmpty String country) {
        List<VirusStatDataHolder> virusData = coronavirusService.getConfirmedCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData, country);
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }
    @RequestMapping("/confirmed/{country}/{state}")
    public VirusDashBoard getConfirmedCountryState(@PathVariable("country") @NotNull @NotEmpty String country,
                                                   @PathVariable("state") @NotNull @NotEmpty String state) {
        List<VirusStatDataHolder> virusData = coronavirusService.getConfirmedCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData, country, state);
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }

    @RequestMapping("/recovered/{country}")
    public VirusDashBoard getRecoveredCountry(@PathVariable("country") @NotNull @NotEmpty String country) {
        List<VirusStatDataHolder> virusData = coronavirusService.getRecoveredCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData, country);
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }
    @RequestMapping("/recovered/{country}/{state}")
    public VirusDashBoard getRecoveredCountryState(@PathVariable("country") @NotNull @NotEmpty String country,
                                                   @PathVariable("state") @NotNull @NotEmpty String state) {
        List<VirusStatDataHolder> virusData = coronavirusService.getRecoveredCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData, country, state);
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }


    @RequestMapping("/death/{country}")
    public VirusDashBoard getDeathCountry(@PathVariable("country") @NotNull @NotEmpty String country) {
        List<VirusStatDataHolder> virusData = coronavirusService.getDeathCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData, country);
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }
    @RequestMapping("/death/{country}/{state}")
    public VirusDashBoard getDeathCountryState(@PathVariable("country") @NotNull @NotEmpty String country,
                                                   @PathVariable("state") @NotNull @NotEmpty String state) {
        List<VirusStatDataHolder> virusData = coronavirusService.getDeathCases();;
        VirusDashBoard virusDashBoard = getVirusDashBoard(virusData, country, state);
        updateLastRecordFetchDate(virusDashBoard);
        return virusDashBoard;
    }

    /**
     * Filter by country
     * @param virusData
     * @param country
     * @return
     */
    private VirusDashBoard getVirusDashBoard(List<VirusStatDataHolder> virusData, String country) {
        VirusDashBoard virusDashBoard = new VirusDashBoard();
        List<VirusStatDataHolder> filteredVirusData =
                virusData.parallelStream().filter(virusStatDataHolder ->
                        country.equalsIgnoreCase(virusStatDataHolder.getCountry())).collect(Collectors.toList());
        virusDashBoard.setVirusStatDataHolderList(filteredVirusData);
        virusDashBoard.setTotalReported(filteredVirusData.stream().
                mapToInt(virusStat -> virusStat.getTotalCases()).sum()); // Total confirmed cases from all regions
        virusDashBoard.setTotalNew(filteredVirusData.stream().
                mapToInt(data -> data.getNewCaseCount()).sum()); // Total # of new cases from all regions

        return virusDashBoard;
    }

    private VirusDashBoard getVirusDashBoard(List<VirusStatDataHolder> virusData, String country, String state) {
        VirusDashBoard virusDashBoard = new VirusDashBoard();
        List<VirusStatDataHolder> filteredVirusData =
                virusData.parallelStream().filter(virusStatDataHolder ->
                        country.equalsIgnoreCase(virusStatDataHolder.getCountry())
                                && state.equalsIgnoreCase(virusStatDataHolder.getState())).collect(Collectors.toList());
        virusDashBoard.setVirusStatDataHolderList(filteredVirusData);
        virusDashBoard.setTotalReported(filteredVirusData.stream().
                mapToInt(virusStat -> virusStat.getTotalCases()).sum()); // Total confirmed cases from all regions
        virusDashBoard.setTotalNew(filteredVirusData.stream().
                mapToInt(data -> data.getNewCaseCount()).sum()); // Total # of new cases from all regions
        return virusDashBoard;
    }
}
