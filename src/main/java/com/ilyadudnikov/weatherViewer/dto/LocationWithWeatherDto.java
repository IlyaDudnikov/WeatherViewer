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
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double feelsLike;
    private String description;
    private String icon;
}
