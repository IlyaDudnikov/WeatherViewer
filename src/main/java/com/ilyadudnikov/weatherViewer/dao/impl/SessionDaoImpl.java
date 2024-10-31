package com.ilyadudnikov.weatherViewer.dao.impl;

import com.ilyadudnikov.weatherViewer.dao.SessionDao;
import com.ilyadudnikov.weatherViewer.models.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionDaoImpl implements SessionDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public SessionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Session session) {
        org.hibernate.Session hibernateSession = sessionFactory.getCurrentSession();
        hibernateSession.persist(session);
    }

    @Override
    public Optional<Session> findById(UUID uuid) {
        org.hibernate.Session hibernateSession = sessionFactory.getCurrentSession();
        Session session = hibernateSession.get(Session.class, uuid);
        return Optional.ofNullable(session);
    }

    @Override
    public void delete(Session session) {
        org.hibernate.Session hibernateSession = sessionFactory.getCurrentSession();
        hibernateSession.remove(session);
    }

    @Override
    public void deleteById(UUID uuid) {
        org.hibernate.Session hibernateSession = sessionFactory.getCurrentSession();
        Session session = hibernateSession.get(Session.class, uuid);
        hibernateSession.remove(session);
    }
}
