package com.mart.utils;

import com.mart.entity.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;

public class TestUtils {

    public static Transaction createTransaction(Integer id, OffsetDateTime transactionTime, BigDecimal purchaseAmount){
        return Transaction.builder()
                .id(BigInteger.valueOf(id))
                .transactionDate(transactionTime)
                .purchaseAmount(purchaseAmount)
                .build();
    }
}
