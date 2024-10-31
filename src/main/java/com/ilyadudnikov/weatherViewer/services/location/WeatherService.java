package com.ilyadudnikov.weatherViewer.services.location;

import com.ilyadudnikov.weatherViewer.exceptions.GeocodingApiException;
import com.ilyadudnikov.weatherViewer.exceptions.WeatherApiException;
import com.ilyadudnikov.weatherViewer.models.Location;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;

import java.util.List;

public interface WeatherService {
    List<LocationApiResponse> getLocationsByName(String name) throws GeocodingApiException;
    LocationWithWeatherApiResponse getLocationWithWeather(Location location) throws WeatherApiException;
}
