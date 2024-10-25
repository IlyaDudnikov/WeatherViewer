package com.ilyadudnikov.weatherViewer.dao.impl;

import com.ilyadudnikov.weatherViewer.dao.UserDao;
import com.ilyadudnikov.weatherViewer.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user;
        try (Session session = sessionFactory.openSession()) {
            user = session.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
//        try (Session session = sessionFactory.openSession()) {
//            session.persist(user);
//        }
    }
}
