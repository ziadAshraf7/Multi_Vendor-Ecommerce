package com.example.ecommerce_app.Exceptions.Exceptions;

public class CustomAuthorizationException extends RuntimeException{
    public CustomAuthorizationException(String message) {
        super(message);
    }
}
