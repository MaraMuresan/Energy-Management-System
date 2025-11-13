package com.example.authenticationmicroservice.controllers.handlers;


import com.example.authenticationmicroservice.controllers.handlers.exceptions.model.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        Set<ConstraintViolation<?>> details = e.getConstraintViolations();
        ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(e.getMessage(),
                status.getReasonPhrase(),
                status.value(),
                e.getMessage(),
                details,
                request.getDescription(false));
        return handleExceptionInternal(
                e,
                errorInformation,
                new HttpHeaders(),
                status,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        String message = details.size() == 1 ? details.get(0) : "Validation failed";

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(
                "RegisterRequestDTO",
                httpStatus.getReasonPhrase(),
                httpStatus.value(),
                message,
                details,
                request.getDescription(false)
        );

        return handleExceptionInternal(ex, errorInformation, new HttpHeaders(), httpStatus, request);
    }


    @ExceptionHandler(value = {ResourceNotFoundException.class, DuplicateResourceException.class, EntityValidationException.class})
    protected ResponseEntity<Object> handleResourceNotFound(CustomException ex,
                                                            WebRequest request) {
        ExceptionHandlerResponseDTO errorInformation = new ExceptionHandlerResponseDTO(ex.getResource(),
                ex.getStatus().getReasonPhrase(),
                ex.getStatus().value(),
                ex.getMessage(),
                ex.getValidationErrors(),
                request.getDescription(false));
        return handleExceptionInternal(
                ex,
                errorInformation,
                new HttpHeaders(),
                ex.getStatus(),
                request
        );
    }


}
