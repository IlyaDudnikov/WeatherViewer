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
    @Mapping(target = "feelsLike", source = "main.feelsLike")
    @Mapping(target = "temperature", source = "main.temperature")
    @Mapping(target = "humidity", source = "main.humidity")
    @Mapping(target = "description", expression = "java(getDescription(response.getWeather()))")
    @Mapping(target = "icon", expression = "java(getIcon(response.getWeather()))")
    @Mapping(target = "country", source = "sys.country")
    LocationWithWeatherDto toDto(LocationWithWeatherApiResponse response);

    default String getDescription(List<Weather> weather) {
        return weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : null;
    }

    default String getIcon(List<Weather> weather) {
        return weather != null && !weather.isEmpty() ? weather.get(0).getIcon() : null;
    }

    List<LocationWithWeatherDto> toDtoList(List<LocationWithWeatherApiResponse> responseList);


}
