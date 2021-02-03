package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.entity.Record;
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
    public User loginUser(@RequestParam String username,
                             @RequestParam String password) {
        log.info("NEW LOGIN: {} with password {}", username, password);
        User loggedInUser = userService.logInUser(username, password);

        return loggedInUser;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        log.info("NEW REGISTRATION: {}", user);
        User newUser = userService.registerUser(user);

        log.debug("USER REGISTERED: {}", newUser);
        return newUser;
    }

    @PostMapping("/record/{id}")
    public boolean addNewRecord(@PathVariable (name = "id") Long id, @RequestBody Record record) {
        log.info("REQUEST BY USER {}, TO ADD NEW RECORD {}", id, record);
        boolean successfulAddition = userService.addingNewRecord(id, record);

        log.debug("REQUEST WAS SUCCESSFUL {}", successfulAddition);
        return successfulAddition;
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        log.info("RECEIVED REQUEST TO LIST ALL USERS.");
        return userService.listAllUsers();
    }

    @GetMapping("/records/{id}")
    public List<Record> getAllRecords(@PathVariable (name = "id") Long id) {
        log.info("REQUEST FOR RECORDS BY ID {}", id);
        List<Record> listOfRecordsFound = userService.retrieveRecords(id);

        log.debug("REQUEST WAS SUCCESSFUL: {}", listOfRecordsFound);
        return listOfRecordsFound;
    }

}