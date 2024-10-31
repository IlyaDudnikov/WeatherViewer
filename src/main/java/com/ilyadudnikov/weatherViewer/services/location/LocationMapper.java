package com.ilyadudnikov.weatherViewer.services.location;

import com.ilyadudnikov.weatherViewer.dto.LocationWithWeatherDto;
import com.ilyadudnikov.weatherViewer.models.api.LocationWithWeatherApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LocationMapper {
    LocationMapper instance = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "latitude", source = "coord.latitude")
    @Mapping(target = "longitude", source = "coord.longitude")
    @Mapping(target = "feelsLike", source = "weather[0].feelsLike")
    @Mapping(target = "description", source = "weather[0].decription")
    @Mapping(target = "icon", source = "weather[0].icon")
    LocationWithWeatherDto toDto(LocationWithWeatherApiResponse response);

    List<LocationWithWeatherDto> toDtoList(List<LocationWithWeatherApiResponse> responseList);

}
