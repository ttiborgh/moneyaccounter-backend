package com.moneyaccounterbackend.service;

import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.entity.User;
import com.moneyaccounterbackend.repository.StockRepository;
import com.moneyaccounterbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @Autowired
    public StockService(StockRepository stockRepository, UserRepository userRepository) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    public Stock createNewStock(Long userId, Stock stock) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        stock.setUser(user);
        Stock savedStock = stockRepository.save(stock);
        user.getListOfStocks().add(savedStock);

        return savedStock;
    }
}