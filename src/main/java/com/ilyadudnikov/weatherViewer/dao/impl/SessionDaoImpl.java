package com.ilyadudnikov.weatherViewer.dao.impl;

import com.ilyadudnikov.weatherViewer.dao.SessionDao;
import com.ilyadudnikov.weatherViewer.models.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
