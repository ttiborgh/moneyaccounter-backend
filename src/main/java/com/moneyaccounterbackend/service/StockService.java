package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.entity.StockPurchaseDetails;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.exception.InvalidStockFormatException;
import com.moneyaccounterbackend.repository.StockPurchaseDetailsRepository;
import com.moneyaccounterbackend.repository.StockRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final StockPurchaseDetailsRepository stockPurchaseDetailsRepository;

    @Autowired
    public StockService(StockRepository stockRepository, UserRepository userRepository, StockPurchaseDetailsRepository stockPurchaseDetailsRepository) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.stockPurchaseDetailsRepository = stockPurchaseDetailsRepository;
    }

    public Stock createNewStockEntry(Long userId, String stockName, Long quantity, Double price) throws InvalidStockFormatException {
        validateStockData(stockName, quantity, price);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Optional<Stock> stock = stockRepository.findByStockName(stockName);

        if (stock.isPresent()) {
            Stock stockToBeUpdated = stock.get();
            StockPurchaseDetails newPurchaseDetails = StockPurchaseDetails.builder()
                    .quantity(quantity)
                    .price(price)
                    .createdAt(LocalDateTime.now())
                    .stock(stockToBeUpdated)
                    .build();
            stockToBeUpdated.getStockPurchaseDetails().add(newPurchaseDetails);

            return stockRepository.save(stockToBeUpdated);
        } else {
            StockPurchaseDetails newPurchaseDetails = StockPurchaseDetails.builder()
                    .quantity(quantity)
                    .price(price)
                    .createdAt(LocalDateTime.now())
                    .build();
            Stock newStock = Stock.builder().stockName(stockName).user(user).build();
            newPurchaseDetails.setStock(newStock);
            newStock.setStockPurchaseDetails(List.of(newPurchaseDetails));

            return stockRepository.save(newStock);
        }
    }

    public List<Stock> listStocks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return stockRepository.findAll().stream().filter(stock -> stock.getUser().getId().equals(user.getId())).collect(Collectors.toList());
    }

    public void validateStockData(String stockName, Long quantity, Double price) throws InvalidStockFormatException {
        if (!StringUtils.hasText(stockName) || stockName.length() > 4 || stockName.length() < 1) {
            throw new InvalidStockFormatException("Stockname has no text or of invalid length: " + stockName);
        }
        if (quantity <= 0) {
            throw new InvalidStockFormatException("Quantity of stock during transaction can't be 0 or lower than that: " + quantity);
        }
        if (price < 0) {
            throw new InvalidStockFormatException("Stock's price is negative: " + price);
        }
    }
}