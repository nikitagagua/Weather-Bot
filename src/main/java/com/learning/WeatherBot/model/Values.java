package com.learning.WeatherBot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Values {
    @JsonProperty("cloudBase")
    private BigDecimal cloudBase;

    @JsonProperty("cloudCeiling")
    private BigDecimal cloudCeiling;

    @JsonProperty("cloudCover")
    private int cloudCover;

    @JsonProperty("dewPoint")
    private BigDecimal dewPoint;

    @JsonProperty("freezingRainIntensity")
    private int freezingRainIntensity;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("precipitationProbability")
    private int precipitationProbability;

    @JsonProperty("pressureSurfaceLevel")
    private BigDecimal pressureSurfaceLevel;

    @JsonProperty("rainIntensity")
    private int rainIntensity;

    @JsonProperty("sleetIntensity")
    private int sleetIntensity;

    @JsonProperty("snowIntensity")
    private int snowIntensity;

    @JsonProperty("temperature")
    private BigDecimal temperature;

    @JsonProperty("temperatureApparent")
    private BigDecimal temperatureApparent;

    @JsonProperty("uvHealthConcern")
    private int uvHealthConcern;

    @JsonProperty("uvIndex")
    private int uvIndex;

    @JsonProperty("visibility")
    private int visibility;

    @JsonProperty("weatherCode")
    private int weatherCode;

    @JsonProperty("windDirection")
    private BigDecimal windDirection;

    @JsonProperty("windGust")
    private BigDecimal windGust;

    @JsonProperty("windSpeed")
    private BigDecimal windSpeed;

}