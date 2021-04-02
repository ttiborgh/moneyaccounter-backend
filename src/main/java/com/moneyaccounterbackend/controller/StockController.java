package com.moneyaccounterbackend.controller;

import com.moneyaccounterbackend.entity.Stock;
import com.moneyaccounterbackend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private StockService stockService;

    @Autowired
    public StockController (StockService stockService) {
        this.stockService = stockService;
    }

}