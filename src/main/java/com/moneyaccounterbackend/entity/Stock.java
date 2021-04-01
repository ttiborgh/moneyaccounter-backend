package com.moneyaccounterbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stock {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String stockName;
    @Column
    private Double numberOfStocks;
}