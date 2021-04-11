package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.repository.RecordRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    private static final long RECORD_ID = 1L;
    private static final long USER_ID = 2L;
    private static final String DESCRIPTION = "Travelling expenses";
    private static final long AMOUNT = 500L;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2021,
            Month.APRIL, 11, 20, 23, 59);
    private static final boolean SPENDING = true;

    @Mock
    private RecordRepository recordRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RecordService recordService;

}