package com.ilyadudnikov.weatherViewer.dao.impl;

import com.ilyadudnikov.weatherViewer.dao.LocationDao;
import com.ilyadudnikov.weatherViewer.models.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDaoImpl implements LocationDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public LocationDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(location);
    }

    @Override
    public void deleteById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Location location = session.getReference(Location.class, id);
        session.remove(location);
    }
}
