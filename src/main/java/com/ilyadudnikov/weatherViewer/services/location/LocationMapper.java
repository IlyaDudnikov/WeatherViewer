package com.ilyadudnikov.weatherViewer.services.location;

import com.ilyadudnikov.weatherViewer.dto.LocationWithWeatherDto;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;
import com.ilyadudnikov.weatherViewer.models.api.entity.Weather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LocationMapper {
    LocationMapper instance = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "latitude", source = "coord.latitude")
    @Mapping(target = "longitude", source = "coord.longitude")
    @Mapping(target = "feelsLike", expression = "java(getFeelsLike(response.getWeather()))")
    @Mapping(target = "description", expression = "java(getDescription(response.getWeather()))")
    @Mapping(target = "icon", expression = "java(getIcon(response.getWeather()))")
    LocationWithWeatherDto toDto(LocationWithWeatherApiResponse response);

    default Double getFeelsLike(List<Weather> weather) {
        return weather != null && !weather.isEmpty() ? weather.get(0).getFeelsLike() : null;
    }

    default String getDescription(List<Weather> weather) {
        return weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : null;
    }

    default String getIcon(List<Weather> weather) {
        return weather != null && !weather.isEmpty() ? weather.get(0).getIcon() : null;
    }

    List<LocationWithWeatherDto> toDtoList(List<LocationWithWeatherApiResponse> responseList);


}
