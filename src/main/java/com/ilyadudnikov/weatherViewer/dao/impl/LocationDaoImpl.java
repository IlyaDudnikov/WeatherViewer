package com.ilyadudnikov.weatherViewer.dao.impl;

import com.ilyadudnikov.weatherViewer.dao.LocationDao;
import com.ilyadudnikov.weatherViewer.models.Location;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class LocationDaoImpl implements LocationDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public LocationDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Location> findByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        Session session = sessionFactory.getCurrentSession();
        BigDecimal threshold = BigDecimal.valueOf(1e-5);

        String hql = "FROM Location WHERE latitude BETWEEN :latMin AND :latMax AND longitude BETWEEN :lonMin AND :lonMax";

        BigDecimal latMin = latitude.subtract(threshold);
        BigDecimal latMax = latitude.add(threshold);
        BigDecimal lonMin = longitude.subtract(threshold);
        BigDecimal lonMax = longitude.add(threshold);

        Location location = session.createQuery(hql, Location.class)
                .setParameter("latMin", latMin)
                .setParameter("latMax", latMax)
                .setParameter("lonMin", lonMin)
                .setParameter("lonMax", lonMax)
                .uniqueResult();

        return Optional.ofNullable(location);
    }

    @Override
    public void save(Location location) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(location);
    }
}
