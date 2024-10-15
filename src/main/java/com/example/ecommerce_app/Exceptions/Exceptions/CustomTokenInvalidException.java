package com.example.ecommerce_app.Exceptions.Exceptions;

public class CustomTokenInvalidException extends RuntimeException{

    public CustomTokenInvalidException(String message) {
        super(message);
    }
}
