package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.service.RecordService;
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
public class AccounterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccounterController.class);
    private final UserService userService;
    private final RecordService recordService;

    @Autowired
    public AccounterController(UserService userService, RecordService recordService) {
        this.userService = userService;
        this.recordService = recordService;
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

    @PostMapping("/record/{userid}")
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewRecord(@PathVariable(name = "userid") Long userId, @RequestBody Record record) {
        LOGGER.info("REQUEST BY USER {}, TO ADD NEW RECORD {}", userId, record);
        User user = recordService.addingNewRecord(userId, record);

        return user;
    }

    @GetMapping("/list")
    public List<User> listUsers() {
        LOGGER.info("RECEIVED REQUEST TO LIST ALL USERS.");

        return userService.listAllUsers();
    }

    @GetMapping("/records/{userId}")
    public List<Record> getAllRecords(@PathVariable(name = "userId") Long userId) {
        LOGGER.info("REQUEST FOR RECORDS BY USERID {}", userId);
        List<Record> listOfRecordsFound = recordService.retrieveRecords(userId);

        LOGGER.debug("REQUEST WAS SUCCESSFUL.");
        return listOfRecordsFound;
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable(name = "userId") Long userId) {
        LOGGER.info("REQUEST FOR USER BY ID {}", userId);
        User foundUser = userService.retrieveUser(userId);

        LOGGER.debug("REQUEST WAS SUCCESSFUL: {}", foundUser);
        return foundUser;
    }

    @DeleteMapping("/deleterecord/{recordid}/{userid}")
    public User deleteRecord(@PathVariable(name = "recordid") Long recordId,
                             @PathVariable(name = "userid") Long userId) {
        LOGGER.info("REQUEST TO DELETE RECORD: {}.", recordId);

        return recordService.deleteRecordById(userId, recordId);
    }
}