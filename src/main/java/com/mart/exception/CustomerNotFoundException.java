package com.mart.exception;

import lombok.Data;

@Data
public class CustomerNotFoundException extends RuntimeException {

    private String message;
    public CustomerNotFoundException(String message){
        super(message);
    }
}
