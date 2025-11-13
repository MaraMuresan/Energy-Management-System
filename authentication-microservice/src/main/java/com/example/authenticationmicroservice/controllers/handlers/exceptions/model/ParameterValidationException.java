package com.example.authenticationmicroservice.controllers.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ParameterValidationException extends CustomException {
    private static final String MESSAGE = "Parameter is invalid!";
    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ParameterValidationException(String resource) {
        super(resource, httpStatus, MESSAGE, new ArrayList<>());
    }
}