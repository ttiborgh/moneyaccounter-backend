package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        log.info("REQUEST TO REGISTER: {}", user);
        User registeredUser = userRepository.save(user);

        log.debug("USER SUCCESSFULLY REGISTERED: {}", registeredUser);
        return registeredUser;
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public boolean logInUser(String username, String password) {
        Optional<User> foundUser = userRepository.findByUsername(username);

        if (foundUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return true;
    }

}