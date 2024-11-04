package com.ilyadudnikov.weatherViewer.services.location;

import com.ilyadudnikov.weatherViewer.dao.LocationDao;
import com.ilyadudnikov.weatherViewer.dto.LocationWithWeatherDto;
import com.ilyadudnikov.weatherViewer.exceptions.GeocodingApiException;
import com.ilyadudnikov.weatherViewer.exceptions.WeatherApiException;
import com.ilyadudnikov.weatherViewer.models.Location;
import com.ilyadudnikov.weatherViewer.models.Session;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final WeatherService weatherService;
    private final LocationDao locationDao;

    @Autowired
    public LocationService(WeatherService weatherService, LocationDao locationDao) {
        this.weatherService = weatherService;
        this.locationDao = locationDao;
    }

    public List<LocationWithWeatherDto> getUserLocationsWithWeatherBySession(Session session) throws WeatherApiException {
        User user = session.getUser();
        List<Location> userLocations = user.getLocations();
        List<LocationWithWeatherApiResponse> locationsWeatherApi = new ArrayList<>();

        for (Location location : userLocations) {
            LocationWithWeatherApiResponse locationWeatherApi = weatherService.getLocationWithWeather(location);

            locationWeatherApi.getCoord().setLatitude(location.getLatitude());
            locationWeatherApi.getCoord().setLongitude(location.getLongitude());

            locationWeatherApi.setId(location.getId());

            locationsWeatherApi.add(locationWeatherApi);
        }

        LocationMapper mapper = LocationMapper.instance;
        return mapper.toDtoList(locationsWeatherApi);
    }

    public List<LocationApiResponse> getLocationsByName(String locationName) throws GeocodingApiException {
        return weatherService.getLocationsByName(locationName);
    }

    @Transactional
    public void add(LocationApiResponse locationApiResponse, Session session) {
        ModelMapper modelMapper = new ModelMapper();
        Location location = modelMapper.map(locationApiResponse, Location.class);
        User user = session.getUser();
        location.setUser(user);
        locationDao.save(location);
    }

    @Transactional
    public void delete(Long locationId) {
        locationDao.deleteById(locationId);
    }
}
