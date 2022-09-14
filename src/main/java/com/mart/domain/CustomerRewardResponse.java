package com.mart.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Month;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
public class CustomerRewardResponse implements Serializable {
    private String firstName;
    private String lastName;
    private BigInteger customerId;
    private Map<Month, BigDecimal> monthlyRewards;
    private BigDecimal totalRewardPoints;
}
