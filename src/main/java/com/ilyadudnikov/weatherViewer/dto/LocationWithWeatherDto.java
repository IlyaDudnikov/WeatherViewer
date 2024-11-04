package com.ilyadudnikov.weatherViewer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationWithWeatherDto {
    private Long id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double feelsLike;
    private Double temperature;
    private Integer humidity;
    private String description;
    private String icon;
    private String country;
}
