package com.example.ecommerce_app.Exceptions.Exceptions;

public class BadRequestException  extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
