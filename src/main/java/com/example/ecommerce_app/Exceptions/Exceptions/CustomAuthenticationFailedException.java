package com.example.ecommerce_app.Exceptions.Exceptions;

public class CustomAuthenticationFailedException extends RuntimeException{

    public CustomAuthenticationFailedException(String message) {
        super(message);
    }
}

