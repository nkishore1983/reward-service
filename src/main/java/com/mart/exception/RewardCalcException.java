package com.mart.exception;

import lombok.Data;

@Data
public class RewardCalcException extends RuntimeException {
    private String message;
    private Throwable throwable;


    public RewardCalcException(String message, Throwable th){
        super(message, th);
    }
}
