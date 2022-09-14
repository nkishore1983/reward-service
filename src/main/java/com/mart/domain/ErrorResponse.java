package com.mart.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
public class ErrorResponse implements Serializable {
    private int errorCode;
    private String errorMessage;
}
