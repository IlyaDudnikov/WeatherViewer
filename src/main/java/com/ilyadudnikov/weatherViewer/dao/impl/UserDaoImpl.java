package com.ilyadudnikov.weatherViewer.dao.impl;

import com.ilyadudnikov.weatherViewer.dao.UserDao;
import com.ilyadudnikov.weatherViewer.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("FROM User WHERE login = :login", User.class)
                .setParameter("login", login)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(user);
    }

    @Override
    public void deleteAll() {
        Session session = sessionFactory.getCurrentSession();
        List<User> users = session.createQuery("FROM User", User.class).getResultList();
        for (User user : users) {
            session.remove(user);
        }
    }
}
