package com.mart.exception;

import lombok.Data;

@Data
public class MartException extends RuntimeException {

    private String message;

    public MartException(String message){
        super(message);
    }
}
