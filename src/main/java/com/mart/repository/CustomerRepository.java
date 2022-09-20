package com.mart.repository;

import com.mart.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, BigInteger> {
}
