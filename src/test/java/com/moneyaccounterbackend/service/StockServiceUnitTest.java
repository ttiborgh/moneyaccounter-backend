package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.entity.StockPurchaseDetails;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.exception.InvalidFormatException;
import com.moneyaccounterbackend.repository.StockRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceUnitTest {

    private static final Long USER_ID = 1L;
    private static final Long STOCK_ID = 2L;
    private static final Long STOCK_PURCHASE_ID = 3L;
    private static final String STOCK_NAME = "AAPL";
    private static final Double STOCK_PRICE = 950.1;
    private static final Long STOCK_QUANTITY = 10L;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2021,
            Month.APRIL, 3, 10, 30, 40);

    @Mock
    private StockRepository stockRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private StockService stockService;

    @Test
    public void givenNonExistingUserId_whenCreatingNewStock_thenExceptionIsThrown() {
        when(userRepository.findById(USER_ID)).thenThrow(ResponseStatusException.class);
        assertThrows(ResponseStatusException.class, () -> stockService.createNewStockEntry(USER_ID, STOCK_NAME, STOCK_QUANTITY, STOCK_PRICE));
    }
}