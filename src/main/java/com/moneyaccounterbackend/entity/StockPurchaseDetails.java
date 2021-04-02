package com.moneyaccounterbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
public class StockPurchaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String date;        // TODO: date format later to be added
    @Column
    private double price;
    @Column
    private long quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Stock stock;
}