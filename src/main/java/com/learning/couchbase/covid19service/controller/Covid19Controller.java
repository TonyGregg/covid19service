package com.learning.couchbase.covid19service.controller;

import com.learning.couchbase.covid19service.model.LocationStat;
import com.learning.couchbase.covid19service.services.CoronavirusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by Antony Genil Gregory on 3/15/2020 6:31 PM
 * For project : covid19service
 **/
@RestController
@RequestMapping("/api/v1/covid19")
@Slf4j
public class Covid19Controller {
    @Autowired
    CoronavirusService coronavirusService;
    @RequestMapping("/all")
    public List<LocationStat> getAllRawData() {
        List<LocationStat> locationStats = null;
        try {
            locationStats =  coronavirusService.fetchVirusData();
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return locationStats;
    }
}
