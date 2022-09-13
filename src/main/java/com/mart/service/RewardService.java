package com.mart.service;

import com.mart.annotation.ElapsedTime;
import com.mart.domain.CustomerRewardResponse;
import com.mart.entity.Customer;
import com.mart.entity.Transaction;
import com.mart.exception.CustomerNotFoundException;
import com.mart.exception.RewardCalcException;
import com.mart.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RewardService {

    @Autowired
    private CustomerRepository customerRepository;

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100.0);
    private static final BigDecimal FIFTY = BigDecimal.valueOf(50.0);

    @ElapsedTime
    public CustomerRewardResponse getRewards(BigInteger customerId) {
        log.info("Calculating Rewards for customer: {}", customerId);
        CustomerRewardResponse customerRewardResponse = CustomerRewardResponse.builder().build();
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id: %s not found", customerId)));
        customerRewardResponse.setFirstName(customer.getFirstName());
        customerRewardResponse.setLastName(customer.getLastName());
        Map<Month, List<Transaction>> monthlyTransactions = customer.getTransactions().stream().collect(Collectors.groupingBy(p -> p.getTransactionDate().getMonth(), Collectors.toList()));
        Map<Month, BigDecimal> monthlyRewardPoints = monthlyTransactions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().map(this::calculatePoints).reduce(BigDecimal.ZERO, BigDecimal::add)));
        customerRewardResponse.setMonthlyRewards(monthlyRewardPoints);
        customerRewardResponse.setTotalRewardPoints(monthlyRewardPoints.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
        return customerRewardResponse;
    }

    private BigDecimal calculatePoints(Transaction transaction) {
        BigDecimal rewardPoints = BigDecimal.ZERO;
        try {
            BigDecimal purchaseAmount = transaction.getPurchaseAmount();
            if (purchaseAmount.compareTo(HUNDRED) > 0) {
                rewardPoints = rewardPoints.add(twoPointRewardsFn.apply(transaction.getPurchaseAmount()));
            } else if (purchaseAmount.compareTo(FIFTY) > 0) {
                rewardPoints = rewardPoints.add(purchaseAmount.subtract(FIFTY));
            }
        } catch (Exception ex) {
            throw new RewardCalcException("Error while calculating reward points", ex);
        }
        return rewardPoints;
    }

    private UnaryOperator<BigDecimal> twoPointRewardsFn = amount -> FIFTY.add(amount.subtract(HUNDRED).multiply(BigDecimal.valueOf(2.0)));

}
