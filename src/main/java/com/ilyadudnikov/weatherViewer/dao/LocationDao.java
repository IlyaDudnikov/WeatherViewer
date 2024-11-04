package com.ilyadudnikov.weatherViewer.dao;

import com.ilyadudnikov.weatherViewer.models.Location;

public interface LocationDao {
    void save(Location location);
    void deleteById(Long id);
}
