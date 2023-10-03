package com.learning.WeatherBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@lombok.Data
public class Data {
    @JsonProperty("time")
    private String time;

    @JsonProperty("values")
    private Values values;
}