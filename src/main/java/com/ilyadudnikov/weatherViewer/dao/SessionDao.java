package com.ilyadudnikov.weatherViewer.dao;

import com.ilyadudnikov.weatherViewer.models.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionDao {
    void save(Session session);
    Optional<Session> findById(UUID uuid);
    void delete(Session session);
}