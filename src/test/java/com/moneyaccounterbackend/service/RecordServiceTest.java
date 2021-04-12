package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Record;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.repository.RecordRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        givenMockedUserRepositoryFindingUserWithoutRecords();
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

    @Test
    public void givenInvalidUserId_whenAddingNewRecord_thenExceptionIsThrown() {
        givenMockedUserRepositoryNotFindingUserById();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> recordService.addingNewRecord(USER_ID, givenValidRecordDTO()));
        assertThat(exception.getStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void givenValidUserId_whenRetrievingAllPersonalRecords_thenListIsReturned() {
        givenMockedUserRepositoryFindingUserWithRecords();

        List<Record> records = recordService.retrieveRecords(USER_ID);

        assertThat(records.size(), is(2));      // since we added two empty records into users list
    }

    @Test
    public void givenInvalidUserId_whenRetrievingAllPersonalRecords_thenExceptionIsThrown() {
        givenMockedUserRepositoryNotFindingUserById();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> recordService.retrieveRecords(USER_ID));
        assertThat(exception.getStatus(), is(HttpStatus.NOT_FOUND));
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

    private User givenExistingUserWithRecords() {
        return User.builder()
                .id(USER_ID)
                .username("User")
                .balance(0L)
                .email("valid@email.com")
                .password("valid")
                .listOfRecords(List.of(new Record(), new Record()))
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

    private void givenMockedUserRepositoryFindingUserWithoutRecords() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(givenExistingUserWithoutRecords()));
    }

    private void givenMockedUserRepositoryFindingUserWithRecords() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(givenExistingUserWithRecords()));
    }

    private void givenMockedUserRepositoryNotFindingUserById() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    }
}