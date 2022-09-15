package com.mart.service;

import com.mart.domain.CustomerRewardResponse;
import com.mart.entity.Customer;
import com.mart.entity.Transaction;
import com.mart.repository.CustomerRepository;
import com.mart.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Customer customer;

    @Spy
    @InjectMocks
    private RewardService underTest = new RewardService();

    @DisplayName("Test Reward Service for purchase amount > 100")
    @Test
    void testRewardsForAmountGreaterThan100() {
        //given
        BigInteger customerId = BigInteger.ONE;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customer.getFirstName()).thenReturn("Tom");
        Mockito.when(customer.getLastName()).thenReturn("David");
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(110.0));
        Transaction tx_2 = TestUtils.createTransaction(2, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(120.0));
        Transaction tx_3 = TestUtils.createTransaction(3, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(150.0));

        Mockito.when(customer.getTransactions()).thenReturn(Arrays.asList(tx_1, tx_2, tx_3));
        //when
        CustomerRewardResponse customerRewardResponse = underTest.getRewards(customerId);
        //then
        assertEquals(BigDecimal.valueOf(310.0).compareTo(customerRewardResponse.getTotalRewardPoints()), 0);
    }

    @DisplayName("Test Reward Service for purchase amount < 100")
    @Test
    void testRewardsForAmountLessThan100() {
        //given
        BigInteger customerId = BigInteger.ONE;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customer.getFirstName()).thenReturn("Tom");
        Mockito.when(customer.getLastName()).thenReturn("David");
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(55.0));
        Mockito.when(customer.getTransactions()).thenReturn(Collections.singletonList(tx_1));
        //when
        CustomerRewardResponse customerRewardResponse = underTest.getRewards(customerId);
        //then
        assertEquals(BigDecimal.valueOf(5.0).compareTo(customerRewardResponse.getTotalRewardPoints()), 0);
    }

    @DisplayName("Test Reward Service for purchase amount = 100")
    @Test
    void testRewardsForAmountEqualTo100() {
        //given
        BigInteger customerId = BigInteger.ONE;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customer.getFirstName()).thenReturn("Tom");
        Mockito.when(customer.getLastName()).thenReturn("David");
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(100.0));
        Mockito.when(customer.getTransactions()).thenReturn(Collections.singletonList(tx_1));
        //when
        CustomerRewardResponse customerRewardResponse = underTest.getRewards(customerId);
        //then
        assertEquals(BigDecimal.valueOf(50.0).compareTo(customerRewardResponse.getTotalRewardPoints()), 0);
    }

    @DisplayName("Test Reward Service for purchase amount = 50")
    @Test
    void testRewardsForAmountEqualTo50() {
        //given
        BigInteger customerId = BigInteger.ONE;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customer.getFirstName()).thenReturn("Tom");
        Mockito.when(customer.getLastName()).thenReturn("David");
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(50.0));
        Mockito.when(customer.getTransactions()).thenReturn(Collections.singletonList(tx_1));
        //when
        CustomerRewardResponse customerRewardResponse = underTest.getRewards(customerId);
        //then
        assertEquals(BigDecimal.valueOf(0.0).compareTo(customerRewardResponse.getTotalRewardPoints()), 0);
    }

    @DisplayName("Test Reward Service for purchase amount = 550")
    @Test
    void testRewardsForBigAmount() {
        //given
        BigInteger customerId = BigInteger.ONE;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customer.getFirstName()).thenReturn("Tom");
        Mockito.when(customer.getLastName()).thenReturn("David");
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(550.0));
        Mockito.when(customer.getTransactions()).thenReturn(Collections.singletonList(tx_1));
        //when
        CustomerRewardResponse customerRewardResponse = underTest.getRewards(customerId);
        //then
        assertEquals(BigDecimal.valueOf(950.0).compareTo(customerRewardResponse.getTotalRewardPoints()), 0);
    }

    @DisplayName("Test Reward Service which yield fraction points")
    @Test
    void testRewardsForFractionPoints() {
        //given
        BigInteger customerId = BigInteger.ONE;
        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customer.getFirstName()).thenReturn("Tom");
        Mockito.when(customer.getLastName()).thenReturn("David");
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(50.59));
        Transaction tx_2 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(71.23));
        Mockito.when(customer.getTransactions()).thenReturn(Arrays.asList(tx_1, tx_2));
        //when
        CustomerRewardResponse customerRewardResponse = underTest.getRewards(customerId);
        //then
        assertEquals(BigDecimal.valueOf(21.82).compareTo(customerRewardResponse.getTotalRewardPoints()), 0);
    }

    @Test
    void testCalculatePointsWhenAmountIs50(){
        //given
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(50.00));
        //when
        BigDecimal points = ReflectionTestUtils.invokeMethod(underTest, "calculatePoints", tx_1);
        //then
        assert points != null;
        assertEquals(BigDecimal.valueOf(0.0).compareTo(points), 0);
    }

    @Test
    void testCalculatePointsWhenAmountIsLessThan50(){
        //given
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(16.0));
        //when
        BigDecimal points = ReflectionTestUtils.invokeMethod(underTest, "calculatePoints", tx_1);
        //then
        assert points != null;
        assertEquals(BigDecimal.valueOf(0.0).compareTo(points), 0);
    }

    @Test
    void testCalculatePointsWhenAmountIsGreaterThan100(){
        //given
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(115.0));
        //when
        BigDecimal points = ReflectionTestUtils.invokeMethod(underTest, "calculatePoints", tx_1);
        //then
        assert points != null;
        assertEquals(BigDecimal.valueOf(80.0).compareTo(points), 0);
    }

    @Test
    void testCalculatePointsWhenAmountIsLessThan100(){
        //given
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(93.0));
        //when
        BigDecimal points = ReflectionTestUtils.invokeMethod(underTest, "calculatePoints", tx_1);
        //then
        assert points != null;
        assertEquals(BigDecimal.valueOf(43.0).compareTo(points), 0);
    }

    @Test
    void testCalculatePointsWhenAmountIsGreaterThan1000(){
        //given
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(1112.0));
        //when
        BigDecimal points = ReflectionTestUtils.invokeMethod(underTest, "calculatePoints", tx_1);
        //then
        assert points != null;
        assertEquals(BigDecimal.valueOf(2074.0).compareTo(points), 0);
    }

    @Test
    void testCalculatePointsWhenAmountIsLessThan1000(){
        //given
        Transaction tx_1 = TestUtils.createTransaction(1, OffsetDateTime.now().minusMonths(1L), BigDecimal.valueOf(623.0));
        //when
        BigDecimal points = ReflectionTestUtils.invokeMethod(underTest, "calculatePoints", tx_1);
        //then
        assert points != null;
        assertEquals(BigDecimal.valueOf(1096.0).compareTo(points), 0);
    }
}