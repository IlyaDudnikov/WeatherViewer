package com.ilyadudnikov.weatherViewer.dao;

import com.ilyadudnikov.weatherViewer.models.Location;

import java.math.BigDecimal;
import java.util.Optional;

public interface LocationDao {
    Optional<Location> findByCoordinates(BigDecimal latitude, BigDecimal longitude);
    void save(Location location);
}
