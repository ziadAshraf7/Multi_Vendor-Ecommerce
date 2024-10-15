package com.example.ecommerce_app.Exceptions.Exceptions;

public class CustomValidationException extends RuntimeException{

    public CustomValidationException(String message) {
        super(message);
    }
}
