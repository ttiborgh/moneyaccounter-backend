package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private StockService stockService;
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    @Autowired
    public StockController (StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/add/{userid}")
    public Stock addNewStock (@PathVariable(name = "userid") Long userId, @RequestBody Stock stock) {
        LOGGER.info("REQUEST TO ADD NEW STOCK BY USER: {}", userId);

        Stock stockCreated = stockService.createNewStock(userId, stock);

        LOGGER.debug("REQUEST TO ADD NEW STOCK SUCCESSFUL.");
        return stockCreated;
    }

}