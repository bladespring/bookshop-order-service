package com.bladespring.bookshop.orderservice.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ValidationError {
    private String message;
    private Map<String, String> errors;

    public ValidationError(Map<String, String> errors) {
        this.errors = errors;
        this.message = "Validation Error";
    }
}

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationError methodArgumentNotValidHandler(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<String, String>();
        ex.getAllErrors().forEach(error -> {
            var errorField = (FieldError) error;
            errors.put(errorField.getField(), errorField.getDefaultMessage());
        });
        return new ValidationError(errors);
    }
}
