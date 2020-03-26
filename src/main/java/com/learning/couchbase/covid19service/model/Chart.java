package com.learning.couchbase.covid19service.model;

import lombok.Data;

/**
 * Created by Antony Genil Gregory on 3/26/2020 3:29 PM
 * For project : covid19service
 **/
public interface Chart {
    String caption = "Number of cases";
    String yaxisname = "# of cases";
    String subcaption = "Cases in the last 1 month";
    String  showhovereffect = "1";
    String drawcrossline = "1";
    String plottooltext = "Cumulative cases : <b>$dataValue</b><br>New Cases : <b>$dataValue</b>";
    String theme = "umber";

}
