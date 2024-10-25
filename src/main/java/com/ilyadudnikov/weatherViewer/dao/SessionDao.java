package com.ilyadudnikov.weatherViewer.dao;

import com.ilyadudnikov.weatherViewer.models.Session;

public interface SessionDao {
    void save(Session session);
}