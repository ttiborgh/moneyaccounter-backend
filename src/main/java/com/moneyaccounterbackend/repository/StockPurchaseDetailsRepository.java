package com.moneyaccounterbackend.repository;

import com.moneyaccounterbackend.entity.StockPurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockPurchaseDetailsRepository extends JpaRepository<StockPurchaseDetails, Long> {
}