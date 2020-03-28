package com.learning.couchbase.covid19service.controller;

import com.learning.couchbase.covid19service.model.Tracker;
import com.learning.couchbase.covid19service.services.DailyTrackerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * Created by Antony Genil Gregory on 3/26/2020 1:58 PM
 * For project : covid19service
 **/
@RestController
@RequestMapping("/api/v2/day_tracker")
@Slf4j
public class DailyTrackController {
    @Autowired
    DailyTrackerService dailyTrackerService;

    @Autowired
    HttpServletRequest request;
    @RequestMapping("/confirmed/{country}")
    public Tracker getTracker(@PathVariable("country") @NotBlank String country) {
        log.info("Daily tracker for country : "+country);
        String ipAddress = request.getRemoteAddr();
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");

        log.info("IP Address of the client : "+ " remote address :: "+ remoteAddr);
        return dailyTrackerService.getTracker(country);
    }

}
