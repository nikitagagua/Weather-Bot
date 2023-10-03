package com.learning.WeatherBot.weatherAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.WeatherBot.model.WeatherData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WeatherAPIService {
    private static String key = "${weather.api}";
    public WeatherData getInfoByCity(String city) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.tomorrow.io/v4/weather/realtime?location="+city+"&apikey="+key))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), WeatherData.class);
    }

    public WeatherData getInfoByLocation(Double lat, Double lon) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.tomorrow.io/v4/weather/realtime?location="
                        +lat.toString() + ",%20"+lon.toString()+"&apikey="+key))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), WeatherData.class);
    }
}
