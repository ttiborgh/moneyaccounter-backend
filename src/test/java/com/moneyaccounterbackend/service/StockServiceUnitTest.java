package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.entity.StockPurchaseDetails;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.exception.InvalidFormatException;
import com.moneyaccounterbackend.repository.StockRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.apache.tomcat.jni.Local;
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
import static org.hamcrest.CoreMatchers.notNullValue;
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

    @Test
    public void givenValidData_whenCreatingWholeNewStock_thenStockIsSaved() throws InvalidFormatException {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(givenValidUser()));
        when(stockRepository.findByStockName(any(String.class))).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenAnswer(invocationOnMock -> {
            Stock stock = invocationOnMock.getArgument(0);                          // TODO: Find another solution later if there is
            stock.setId(STOCK_ID);
            stock.getStockPurchaseDetails().get(0).setId(STOCK_PURCHASE_ID);
            return stock;
        });

        Stock createdStock = stockService.createNewStockEntry(USER_ID, STOCK_NAME, STOCK_QUANTITY, STOCK_PRICE);

        assertThat(createdStock, notNullValue());
        assertThat(createdStock.getId(), is(STOCK_ID));
        assertThat(createdStock.getUser().getId(), is(USER_ID));
        assertThat(createdStock.getStockName(), is(STOCK_NAME));
        assertThat(createdStock.getStockPurchaseDetails(), notNullValue());
        assertThat(createdStock.getStockPurchaseDetails().get(0).getId(), is(STOCK_PURCHASE_ID));
        assertThat(createdStock.getStockPurchaseDetails().get(0).getStock().getId(), is(STOCK_ID));
        assertThat(createdStock.getStockPurchaseDetails().get(0).getPrice(), is(STOCK_PRICE));
        assertThat(createdStock.getStockPurchaseDetails().get(0).getQuantity(), is(STOCK_QUANTITY));
        assertThat(createdStock.getStockPurchaseDetails().get(0).getCreatedAt(), notNullValue());

        verify(userRepository, times(1)).findById(USER_ID);
    }

    private User givenValidUser() {
        return User.builder()
                .id(USER_ID)
                .username("User")
                .balance(0L)
                .email("valid@email.com")
                .password("valid")
                .build();
    }

    private Stock givenValidStock() {
        return Stock.builder()
                .id(STOCK_ID)
                .user(givenValidUser())
                .stockName(STOCK_NAME)
                .stockPurchaseDetails(givenListOfPurchaseDetails())
                .build();
    }

    private List<StockPurchaseDetails> givenListOfPurchaseDetails() {
        return List.of(StockPurchaseDetails.builder()
                .id(STOCK_PURCHASE_ID)
                .createdAt(CREATED_AT)
                .price(STOCK_PRICE)
                .quantity(STOCK_QUANTITY)
                .stock(givenValidStock())
                .build());
    }

    private StockPurchaseDetails givenPurchaseDetails() {
        return StockPurchaseDetails.builder()
                .id(STOCK_PURCHASE_ID)
                .createdAt(CREATED_AT)
                .price(STOCK_PRICE)
                .quantity(STOCK_QUANTITY)
                .stock(givenValidStock())
                .build();
    }
}