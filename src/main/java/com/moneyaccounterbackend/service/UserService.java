package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.repository.RecordRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    @Autowired
    public UserService(UserRepository userRepository, RecordRepository recordRepository) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
    }

    public User registerUser(User user) {
        LOGGER.info("REQUEST TO REGISTER: {}", user);
        Optional<User> registeredUserByEmail = userRepository.findByEmail(user.getEmail());
        Optional<User> registeredUserByPassword = userRepository.findByUsername(user.getUsername());

        if (registeredUserByEmail.isPresent() || registeredUserByPassword.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        user.setRegisteredAt(LocalDateTime.now());
        User registeredUser = userRepository.save(user);

        LOGGER.debug("USER SUCCESSFULLY REGISTERED: {}", registeredUser);
        return registeredUser;
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public User logInUser(String username, String password) {
        Optional<User> foundUserByName = userRepository.findByUsername(username);
        Optional<User> foundUserByPassword = userRepository.findByPassword(password);

        if (foundUserByName.isPresent() && foundUserByPassword.isPresent()) {
            if (foundUserByName.get().getId().equals(foundUserByPassword.get().getId())) {
                return foundUserByName.get();
            }
        }
        LOGGER.info("USER NOT FOUND.");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public User retrieveUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        calculatingNewBalanceOfUser(user.get());
        return user.get();
    }

    public void calculatingNewBalanceOfUser(User user) {
        LOGGER.info("CALCULATING BALANCE:");
        List<Record> records = List.copyOf(user.getListOfRecords());
        Long sumOfRecords = records.stream().mapToLong(record -> record.getSpending() ? (record.getAmount() * -1L) : record.getAmount()).sum();

        user.setBalance(sumOfRecords);
        userRepository.save(user);
        LOGGER.debug("THE RESULT IS: {}", sumOfRecords);
    }
}