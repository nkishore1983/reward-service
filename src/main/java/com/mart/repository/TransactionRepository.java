package com.mart.repository;

import com.mart.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
}
