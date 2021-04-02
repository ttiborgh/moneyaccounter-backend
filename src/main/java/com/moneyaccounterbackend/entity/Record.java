package com.moneyaccounterbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String description;
    @Column
    private Long amount;
    @Column
    private Boolean spending;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}