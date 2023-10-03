package com.learning.WeatherBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@lombok.Data
public class WeatherData {
    @JsonProperty("data")
    private Data data;

    @JsonIgnore
    private Location location;
}