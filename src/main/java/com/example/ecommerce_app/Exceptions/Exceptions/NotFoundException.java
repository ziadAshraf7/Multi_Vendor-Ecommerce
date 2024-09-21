package com.example.ecommerce_app.Exceptions.Exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
