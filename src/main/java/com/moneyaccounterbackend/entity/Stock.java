package com.moneyaccounterbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String stockName;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "stock")
    private List<StockPurchaseDetails> stockPurchaseDetails;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}