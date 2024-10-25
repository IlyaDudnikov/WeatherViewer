package com.ilyadudnikov.weatherViewer.services;

import com.ilyadudnikov.weatherViewer.config.Utils;
import com.ilyadudnikov.weatherViewer.models.Session;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.dao.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionDao sessionDao;

    @Autowired
    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Transactional
    public UUID create(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(ZonedDateTime.now().plusMinutes(Utils.SESSION_TIME_IN_MINUTES));
        sessionDao.save(session);
        return session.getId();
    }
}
