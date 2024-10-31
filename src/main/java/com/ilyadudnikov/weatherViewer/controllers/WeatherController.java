package com.ilyadudnikov.weatherViewer.controllers;

import com.ilyadudnikov.weatherViewer.dto.LocationWithWeatherDto;
import com.ilyadudnikov.weatherViewer.dto.UserDto;
import com.ilyadudnikov.weatherViewer.exceptions.GeocodingApiException;
import com.ilyadudnikov.weatherViewer.exceptions.NoValidSessionException;
import com.ilyadudnikov.weatherViewer.exceptions.SessionNotFoundException;
import com.ilyadudnikov.weatherViewer.exceptions.WeatherApiException;
import com.ilyadudnikov.weatherViewer.models.Session;
import com.ilyadudnikov.weatherViewer.models.api.LocationApiResponse;
import com.ilyadudnikov.weatherViewer.services.SessionService;
import com.ilyadudnikov.weatherViewer.services.UserService;
import com.ilyadudnikov.weatherViewer.services.location.LocationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WeatherController {
    private final SessionService sessionService;
    private final LocationService locationService;
    private final UserService userService;

    @Autowired
    public WeatherController(SessionService sessionService, LocationService locationService, UserService userService) {
        this.sessionService = sessionService;
        this.locationService = locationService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(@CookieValue(value="session_id", required = false) String sessionId,
                        HttpServletResponse response, Model model) throws WeatherApiException {
        Session session;
        try {
            session = getValidSession(sessionId, response);
        } catch(NoValidSessionException e) {
            return "redirect:/login";
        }

        UserDto user = userService.getUserBySession(session);
        model.addAttribute("user", user);

        List<LocationWithWeatherDto> locationsWithWeather = locationService.getUserLocationsWithWeatherBySession(session);
        model.addAttribute("locations", locationsWithWeather);

        return "indexView";
    }

    @GetMapping
    public String search(@CookieValue(value="session_id", required = false) String sessionId,
                         @RequestParam(value = "q", required = false) String locationName,
                         HttpServletResponse response, Model model) throws GeocodingApiException {
        Session session;
        try {
            session = getValidSession(sessionId, response);
        } catch(NoValidSessionException e) {
            return "redirect:/login";
        }

        UserDto user = userService.getUserBySession(session);
        model.addAttribute("user", user);

        List<LocationApiResponse> foundLocations = locationService.getLocationsByName(locationName);
        model.addAttribute("locations", foundLocations);

        return "searchView";
    }

    private Session getValidSession(String sessionId, HttpServletResponse response) throws NoValidSessionException {
        if (sessionId == null) {
            throw new NoValidSessionException("session_id in null");
        }

        Session session;
        try {
            session = sessionService.getSession(sessionId);
        } catch (SessionNotFoundException e) {
            deleteCookie(response, "session_id");
            throw new NoValidSessionException(e.getMessage());
        }

        if (sessionService.isSessionExpired(session)) {
            sessionService.delete(session);
            deleteCookie(response, "session_id");
            throw new NoValidSessionException("The session has expired");
        }

        return session;
    }

    private void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}