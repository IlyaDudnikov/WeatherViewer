package com.ilyadudnikov.weatherViewer.controllers;

import com.ilyadudnikov.weatherViewer.config.testConfig.TestWebConfig;
import com.ilyadudnikov.weatherViewer.models.User;
import com.ilyadudnikov.weatherViewer.services.SessionService;
import com.ilyadudnikov.weatherViewer.services.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestWebConfig.class})
public class UserControllerIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        UserController userController = new UserController(userService, sessionService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userService.deleteAll();
    }

    @Test
    void testTest() throws Exception {
        mockMvc.perform(get("/login").cookie(new Cookie("session_id", "someSessionId")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getLoginPage_withSessionId_ShouldRedirectToHome() throws Exception {
        mockMvc.perform(get("/login").cookie(new Cookie("session_id", "someSessionId")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void getLoginPage_withoutSessionId_ShouldShowLoinPage() throws Exception {
        mockMvc.perform(get("/login").cookie(new Cookie("session_id", "")))
                .andExpect(status().isOk())
                .andExpect(view().name("loginView"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void postLoginPage_withValidCredentials_ShouldSetSessionCookie() throws Exception {
        User user = User.builder().login("login").password("password").build();
        userService.register(user);

        mockMvc.perform(post("/login")
                        .param("login", "login")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(cookie().exists("session_id"));
    }

    @Test
    void postLoginPage_withInvalidCredentials_ShouldReturnLoginError() throws Exception {
        User user = User.builder().login("login").password("password").build();
        userService.register(user);

        mockMvc.perform(post("/login")
                        .param("login", "login")
                        .param("password", "invalidPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginView"))
                .andExpect(model().attribute("org.springframework.validation.BindingResult.user",
                        hasProperty("globalErrors", hasItem(
                                hasProperty("code", is("loginError"))
                        ))
                ));
    }

    @Test
    void getRegisterPage_WithSessionId_ShouldRedirectToHome() throws Exception {
        mockMvc.perform(get("/register").cookie(new Cookie("session_id", "someSessionId")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void getRegisterPage_WithoutSessionId_ShouldShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerView"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void postRegisterPage_withExistingUser_ShouldReturnRegisterError() throws Exception {
        User user = User.builder().login("login").password("password").build();
        userService.register(user);

        mockMvc.perform(post("/register")
                        .param("login", "login")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerView"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("org.springframework.validation.BindingResult.user",
                        hasProperty("globalErrors", hasItem(
                                hasProperty("code", is("registerError"))
                        ))
                ));
    }

    @Test
    void postRegisterPage_WithNewUser_ShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/register")
                .param("login", "login")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void postRegisterPage_WithNewUser_ShouldAddedUserToDatabase() throws Exception {
        String login = "login";
        String password = "password";

        mockMvc.perform(post("/register")
                .param("login", login)
                .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        User registeredUser = userService.findUserByLoginAndPassword(login, password);
        assertThat(registeredUser, notNullValue());
        assertThat(registeredUser.getLogin(), is(login));
    }
}
