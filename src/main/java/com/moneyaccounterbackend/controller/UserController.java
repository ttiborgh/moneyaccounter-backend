package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.service.UserService;
import com.moneyaccounterbackend.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User loginUser(@RequestParam String username,
                          @RequestParam String password) {
        LOGGER.info("NEW LOGIN: {} with password {}", username, password);
        User loggedInUser = userService.logInUser(username, password);

        LOGGER.debug("THE USER LOGGED IN IS {}", loggedInUser);
        return loggedInUser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody User user) {
        LOGGER.info("NEW REGISTRATION: {}", user);
        User newUser = userService.registerUser(user);

        LOGGER.debug("USER REGISTERED: {}", newUser);
        return newUser;
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        LOGGER.info("RECEIVED REQUEST TO LIST ALL USERS.");

        return userService.listAllUsers();
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable(name = "userId") Long userId) {
        LOGGER.info("REQUEST FOR USER BY ID {}", userId);
        User foundUser = userService.retrieveUser(userId);

        LOGGER.debug("REQUEST WAS SUCCESSFUL: {}", foundUser);
        return foundUser;
    }
}