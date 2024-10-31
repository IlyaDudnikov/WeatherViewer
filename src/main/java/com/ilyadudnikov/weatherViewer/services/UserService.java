package com.ilyadudnikov.weatherViewer.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ilyadudnikov.weatherViewer.dto.UserDto;
import com.ilyadudnikov.weatherViewer.exceptions.UserAlreadyExistException;
import com.ilyadudnikov.weatherViewer.models.Location;
import com.ilyadudnikov.weatherViewer.models.Session;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.dao.UserDao;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public User findUserByLoginAndPassword(String login, String password) {
        Optional<User> foundUser = userDao.findByLogin(login);
        if (foundUser.isPresent()) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), foundUser.get().getPassword());
            if (result.verified) {
                return foundUser.get();
            }
        }

        return null;
    }

    @Transactional
    public void register(User user) throws UserAlreadyExistException {
        Optional<User> foundUser = userDao.findByLogin(user.getLogin());
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistException("User with this login already exist");
        }

        String encodedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(encodedPassword);
        userDao.save(user);
    }

    @Transactional
    public void addLocationBySession(LocationApiResponse locationApiResponse, Session session) {
        ModelMapper modelMapper = new ModelMapper();
        Location location = modelMapper.map(locationApiResponse, Location.class);
        User user = session.getUser();
        user.getLocations().add(location);
        userDao.save(user);
    }

    public UserDto getUserBySession(Session session) {
        User user = session.getUser();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public void deleteAll() {
        userDao.deleteAll();
    }
}
