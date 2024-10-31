package com.ilyadudnikov.weatherViewer.services;

import com.ilyadudnikov.weatherViewer.config.Utils;
import com.ilyadudnikov.weatherViewer.exceptions.SessionNotFoundException;
import com.ilyadudnikov.weatherViewer.models.Session;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.dao.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
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

    @Transactional
    public Session getSession(String sessionUuidString) throws SessionNotFoundException {
        UUID sessionUuid = UUID.fromString(sessionUuidString);
        Optional<Session> session = sessionDao.findById(sessionUuid);
        if (session.isPresent()) {
            return session.get();
        } else {
            throw new SessionNotFoundException("Session with this uuid was not found: " + sessionUuid);
        }
    }

    @Transactional
    public void delete(Session session) {
        sessionDao.delete(session);
    }

    @Transactional
    public void deleteById(String sessionIdString) {
        UUID sessionId = UUID.fromString(sessionIdString);
        sessionDao.deleteById(sessionId);
    }

    public boolean isSessionExpired(Session session) {
        return ZonedDateTime.now().isAfter(session.getExpiresAt());
    }
}
