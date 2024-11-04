package com.ilyadudnikov.weatherViewer.models.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
    @JsonProperty("feels_like")
    private Double feelsLike;

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("humidity")
    private Integer humidity;
}
