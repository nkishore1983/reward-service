package com.mart.controller;

import com.mart.domain.CustomerRewardResponse;
import com.mart.exception.CustomerNotFoundException;
import com.mart.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Month;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CustomerRewardsControllerUnitTest {

    @Mock
    private RewardService rewardService;

    @Spy
    @InjectMocks
    private CustomerRewardsController underTest = new CustomerRewardsController();

    @DisplayName("Test Reward Service for success scenario")
    @Test
    public void testRetrieveRewardsSuccess(){
        //given
        BigInteger customerId = BigInteger.ONE;
        BigDecimal expectedTotalRewardPoints = BigDecimal.valueOf(200.0);
        when(rewardService.getRewards(customerId)).thenReturn(CustomerRewardResponse.builder()
                .customerId(customerId)
                .firstName("Ricky")
                .lastName("ponting")
                .totalRewardPoints(expectedTotalRewardPoints)
                .monthlyRewards(Map.of(Month.AUGUST, BigDecimal.valueOf(50.0), Month.JULY, BigDecimal.valueOf(45.0)))
                .build());
        //when
        ResponseEntity<?> response = underTest.retrieveRewards(customerId);
        //then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        CustomerRewardResponse rewardResponse = (CustomerRewardResponse) response.getBody();
        assertNotNull(rewardResponse);
        assertEquals(expectedTotalRewardPoints.compareTo(rewardResponse.getTotalRewardPoints()), 0);
        verify(rewardService, times(1)).getRewards(customerId);

    }

    @DisplayName("Test Reward Service for no reward points")
    @Test
    public void testRetrieveRewardsFailure(){
        //given
        BigInteger customerId = BigInteger.ONE;
        when(rewardService.getRewards(customerId)).thenReturn(CustomerRewardResponse.builder()
                .customerId(customerId)
                .firstName("Ricky")
                .lastName("ponting")
                .totalRewardPoints(BigDecimal.ZERO)
                .build());
        //when
        ResponseEntity<?> response = underTest.retrieveRewards(customerId);
        //then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        CustomerRewardResponse rewardResponse = (CustomerRewardResponse) response.getBody();
        assertNotNull(rewardResponse);
        assertEquals(BigDecimal.ZERO.compareTo(rewardResponse.getTotalRewardPoints()), 0);
        verify(rewardService, times(1)).getRewards(customerId);
    }
}
