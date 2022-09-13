package com.mart.controller;

import com.mart.service.RewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@Slf4j
@RestController
@Validated
@RequestMapping("/customers")
public class CustomerRewardsController {

    @Autowired
    private RewardService rewardService;

    @GetMapping(value = "/{customerId}/rewards")
    public ResponseEntity<?> retrieveRewards(@PathVariable BigInteger customerId) throws Exception{
        var response = rewardService.getRewards(customerId);
        return ResponseEntity.ok(response);
    }
}
