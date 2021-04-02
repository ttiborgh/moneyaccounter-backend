package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.repository.RecordRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final UserService userService;

    @Autowired
    public RecordService(UserRepository userRepository, RecordRepository recordRepository, UserService userService) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.userService = userService;
    }

    public User addingNewRecord(Long id, Record record) {
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            record.setUser(user);
            record.setCreatedAt(LocalDateTime.now());
            recordRepository.save(record);
            user.getListOfRecords().add(record);
            return userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Record> retrieveRecords(Long id) {
        Optional<User> foundUser = userRepository.findById(id);

        if(foundUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return foundUser.get().getListOfRecords();
    }

    public User deleteRecordById(Long userId, Long recordId) {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            recordRepository.deleteById(recordId);
            userService.calculatingNewBalanceOfUser(user.get());
        }

        return user.get();
    }
}