package com.ilyadudnikov.weatherViewer.dao;

import com.ilyadudnikov.weatherViewer.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByLogin(String login);
    void save(User user);
    void deleteAll();
}
