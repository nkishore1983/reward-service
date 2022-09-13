package com.mart.repository;

import com.mart.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
}
