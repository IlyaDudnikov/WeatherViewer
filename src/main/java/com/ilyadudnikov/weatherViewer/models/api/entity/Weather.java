package com.ilyadudnikov.weatherViewer.models.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    @JsonProperty("feels_like")
    private Double feelsLike;

    @JsonProperty("description")
    private String description;

    @JsonProperty("icon")
    private String icon;
}