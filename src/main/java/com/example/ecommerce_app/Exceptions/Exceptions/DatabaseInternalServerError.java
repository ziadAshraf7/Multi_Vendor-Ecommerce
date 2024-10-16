package com.example.ecommerce_app.Exceptions.Exceptions;

public class DatabaseInternalServerError extends RuntimeException{

    public DatabaseInternalServerError(String message) {
        super(message);
    }
}
