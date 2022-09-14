package com.mart.exception;

import com.mart.domain.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {RewardCalcException.class, MartException.class})
    public ErrorResponse handleServiceException(Exception ex){
        return ErrorResponse.builder().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = CustomerNotFoundException.class)
    public ErrorResponse handleServiceException(CustomerNotFoundException ex){
        return ErrorResponse.builder().errorCode(HttpStatus.NOT_FOUND.value()).errorMessage(ex.getMessage()).build();
    }


}
