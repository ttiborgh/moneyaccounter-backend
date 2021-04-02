package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.service.RecordService;
import com.moneyaccounterbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }
    
    @PostMapping("/record/{userid}")
    @ResponseStatus(HttpStatus.CREATED)
    public User addNewRecord(@PathVariable(name = "userid") Long userId, @RequestBody Record record) {
        LOGGER.info("REQUEST BY USER {}, TO ADD NEW RECORD {}", userId, record);

        return recordService.addingNewRecord(userId, record);
    }

    @GetMapping("/records/{userId}")
    public List<Record> getAllRecords(@PathVariable(name = "userId") Long userId) {
        LOGGER.info("REQUEST FOR RECORDS BY USERID {}.", userId);
        List<Record> listOfRecordsFound = recordService.retrieveRecords(userId);

        LOGGER.debug("REQUEST WAS SUCCESSFUL.");
        return listOfRecordsFound;
    }

    @DeleteMapping("/deleterecord/{recordid}/{userid}")
    public User deleteRecord(@PathVariable(name = "recordid") Long recordId,
                             @PathVariable(name = "userid") Long userId) {
        LOGGER.info("REQUEST TO DELETE RECORD: {}.", recordId);

        return recordService.deleteRecordById(userId, recordId);
    }
}