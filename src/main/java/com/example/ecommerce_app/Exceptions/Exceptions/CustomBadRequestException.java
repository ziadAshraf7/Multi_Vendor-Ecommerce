package com.example.ecommerce_app.Exceptions.Exceptions;

public class CustomBadRequestException extends RuntimeException{
    public CustomBadRequestException(String message) {
        super(message);
    }
}
