package com.ilyadudnikov.weatherViewer.models.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilyadudnikov.weatherViewer.models.api.entity.Coord;
import com.ilyadudnikov.weatherViewer.models.api.entity.Main;
import com.ilyadudnikov.weatherViewer.models.api.entity.Sys;
import com.ilyadudnikov.weatherViewer.models.api.entity.Weather;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationWithWeatherApiResponse {
    @JsonIgnore
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("coord")
    private Coord coord;

    @JsonProperty("weather")
    private List<Weather> weather;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("sys")
    private Sys sys;
}
