package com.ilyadudnikov.weatherViewer.services.location;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilyadudnikov.weatherViewer.exceptions.GeocodingApiException;
import com.ilyadudnikov.weatherViewer.exceptions.WeatherApiException;
import com.ilyadudnikov.weatherViewer.models.Location;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OpenWeatherApiService implements WeatherService {
    private static final String APPID = System.getenv("OPEN_WEATHER_MAP_APPID");
    private static final String BASE_API_URL = "https://api.openweathermap.org";
    private static final String GEOCODING_API_URL_SUFFIX = "/geo/1.0/direct";
    private static final String WEATHER_API_URL_SUFFIX = "/data/2.5/weather";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<LocationApiResponse> getLocationsByName(String name) throws GeocodingApiException {
        try {
            URI uri = buildUriForGeocodingRequest(name);
            HttpRequest request = buildRequest(uri);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new GeocodingApiException("Failed to get locations: " + response.body());
            }

            return objectMapper.readValue(response.body(), new TypeReference<>() {});

        } catch (Exception e) {
            throw new GeocodingApiException(e.getMessage());
        }
    }

    @Override
    public LocationWithWeatherApiResponse getLocationWithWeather(Location location) throws WeatherApiException {
        try {
            URI uri = buildUriForWeatherRequest(location.getLatitude(), location.getLongitude());
            HttpRequest request = buildRequest(uri);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new WeatherApiException("Failed to get weather: " + response.body());
            }

            return objectMapper.readValue(response.body(), LocationWithWeatherApiResponse.class);
        } catch (Exception e) {
            throw new WeatherApiException(e.getMessage());
        }
    }

    private HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.of(5, ChronoUnit.SECONDS))
                .build();
    }

    private URI buildUriForGeocodingRequest(String locationName) {
        return URI.create(BASE_API_URL + GEOCODING_API_URL_SUFFIX +
                "?q=" + URLEncoder.encode(locationName, StandardCharsets.UTF_8) +
                "&appid=" + APPID +
                "&units=metric"
        );
    }

    private URI buildUriForWeatherRequest(BigDecimal latitude, BigDecimal longitude) {
        return URI.create(BASE_API_URL + WEATHER_API_URL_SUFFIX +
                "?lat=" + latitude +
                "&lon=" + longitude +
                "&appid=" + APPID +
                "&units=metric"
        );
    }
}
