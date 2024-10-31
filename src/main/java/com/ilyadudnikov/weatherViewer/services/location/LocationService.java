package com.ilyadudnikov.weatherViewer.services.location;

import com.ilyadudnikov.weatherViewer.dto.LocationWithWeatherDto;
import com.ilyadudnikov.weatherViewer.exceptions.GeocodingApiException;
import com.ilyadudnikov.weatherViewer.exceptions.WeatherApiException;
import com.ilyadudnikov.weatherViewer.models.Location;
import com.ilyadudnikov.weatherViewer.models.Session;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final WeatherService weatherService;

    @Autowired
    public LocationService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public List<LocationWithWeatherDto> getUserLocationsWithWeatherBySession(Session session) throws WeatherApiException {
        User user = session.getUser();
        List<Location> userLocations = user.getLocations();
        List<LocationWithWeatherApiResponse> locationsWeatherApi = new ArrayList<>();

        for (Location location : userLocations) {
            LocationWithWeatherApiResponse locationWeatherApi = weatherService.getLocationWithWeather(location);
            locationsWeatherApi.add(locationWeatherApi);
        }

        LocationMapper mapper = LocationMapper.instance;
        return mapper.toDtoList(locationsWeatherApi);
    }

    public List<LocationApiResponse> getLocationsByName(String locationName) throws GeocodingApiException {
        return weatherService.getLocationsByName(locationName);
    }
}
