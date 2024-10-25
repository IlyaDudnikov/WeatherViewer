package com.ilyadudnikov.weatherViewer.controllers;

import com.ilyadudnikov.weatherViewer.exceptions.UserAlreadyExistException;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.services.SessionService;
import com.ilyadudnikov.weatherViewer.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "session_id", defaultValue = "") String sessionUuid,
                        @RequestParam(value = "redirect", required = false) String redirectUrl,
                        Model model) {
        if (!sessionUuid.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            model.addAttribute("redirectUrl", redirectUrl);
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute @Valid User user, BindingResult bindingResult,
                            @RequestParam(value = "redirect", required = false) String redirectUrl,
                            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        User foundUser = userService.findUserByLoginAndPassword(user.getLogin(), user.getPassword());
        if (foundUser == null) {
            bindingResult.reject("loginError", "Invalid login or password");
            return "login";
        }

        UUID sessionUuid = sessionService.create(foundUser);
        Cookie cookie = new Cookie("session_id", sessionUuid.toString());
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);

        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            return "redirect:" + redirectUrl;
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(@CookieValue(value="session_id", defaultValue = "") String sessionUuid,
                           Model model) {
        if (!sessionUuid.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        User foundUser = userService.findUserByLoginAndPassword(user.getLogin(), user.getPassword());
        if (foundUser != null) {
            bindingResult.reject("loginError", "User with this login already exists");
            return "register";
        }

        try {
            userService.register(user);
        } catch(UserAlreadyExistException e) {
            bindingResult.reject("loginError", "User with this login already exists");
            return "register";
        }

        return "redirect:/login";
    }
}
