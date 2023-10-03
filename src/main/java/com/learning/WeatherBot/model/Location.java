package com.learning.WeatherBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Location {
    @JsonProperty("lat")
    private BigDecimal lat;
    @JsonProperty("lon")
    private BigDecimal lon;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
}
