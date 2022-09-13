package com.mart.exception;

public class MartException extends RuntimeException {

    private String message;

    public MartException(String message){
        super(message);
    }
}
