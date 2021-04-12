package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.repository.RecordRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

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

    @Test
    public void givenValidUserIdAndRecordData_whenAddingNewRecordByUser_thenRecordIsCreated() {
        givenMockedUserRepositoryFindingUser();
        when(recordRepository.save(any(Record.class))).thenAnswer(invocationOnMock -> {
            Record record = invocationOnMock.getArgument(0);
            record.setId(RECORD_ID);
            return record;
        });

        Record record = recordService.addingNewRecord(USER_ID, givenValidRecordDTO());

        assertThat(record.getId(), is(RECORD_ID));
        assertThat(record.getUser().getId(), is(USER_ID));
        assertThat(record.getAmount(), is(AMOUNT));
        assertThat(record.getDescription(), is(DESCRIPTION));
        assertThat(record.getCreatedAt(), notNullValue());
        assertThat(record.getSpending(), is(SPENDING));

        verify(recordRepository, times(1)).save(any(Record.class));
        verify(userRepository, times(1)).findById(USER_ID);
    }

    private User givenExistingUserWithoutRecords() {
        return User.builder()
                .id(USER_ID)
                .username("User")
                .balance(0L)
                .email("valid@email.com")
                .password("valid")
                .listOfRecords(new ArrayList<>())
                .build();
    }

    private Record givenValidRecordDTO() {
        return Record.builder()
                .amount(AMOUNT)
                .spending(SPENDING)
                .description(DESCRIPTION)
                .createdAt(CREATED_AT)
                .build();
    }

    private void givenMockedUserRepositoryFindingUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(givenExistingUserWithoutRecords()));
    }
}