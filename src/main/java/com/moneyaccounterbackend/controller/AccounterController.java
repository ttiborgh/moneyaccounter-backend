package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.service.UserService;
import com.moneyaccounterbackend.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccounterController {

    private static final Logger log = LoggerFactory.getLogger(AccounterController.class);
    private final UserService userService;

    @Autowired
    public AccounterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestParam String username,
                             @RequestParam String password) {
        log.info("NEW LOGIN: {} with password {}", username, password);
        return userService.logInUser(username, password);
    }

    @PostMapping("/register")
    public boolean registerUser(@RequestBody User user) {
        log.info("NEW REGISTRATION: {}", user);
        User newUser = userService.registerUser(user);

        log.debug("USER REGISTERED: {}", newUser);
        return true;
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        return userService.listAllUsers();
    }

}