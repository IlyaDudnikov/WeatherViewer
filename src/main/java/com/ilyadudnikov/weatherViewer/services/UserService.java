package com.ilyadudnikov.weatherViewer.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ilyadudnikov.weatherViewer.exceptions.UserAlreadyExistException;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.dao.UserDao;
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
}
