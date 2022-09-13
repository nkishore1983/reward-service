package com.mart.controller;

import com.mart.RewardsAppApplication;
import com.mart.domain.CustomerRewardResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {RewardsAppApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerRewardsControllerTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    void retrieveRewards() {
        //given
        String url = "http://localhost:"+port+"/customers/2/rewards";
        //when
        ResponseEntity<CustomerRewardResponse> response = restTemplate.getForEntity(url, CustomerRewardResponse.class);
        //then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void retrieveRewardsInvalidCustomer() {
        //given
        String url = "http://localhost:"+port+"/customers/12345/rewards";
        //when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        //then
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}