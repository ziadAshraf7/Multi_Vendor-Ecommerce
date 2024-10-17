package com.example.ecommerce_app.Exceptions;


import com.example.ecommerce_app.Exceptions.ErrorResponses.ErrorResponse;
import com.example.ecommerce_app.Exceptions.ErrorResponses.ValidationErrorResponse;
import com.example.ecommerce_app.Exceptions.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionsHandler {



    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(CustomBadRequestException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage() , HttpStatus.BAD_REQUEST.value()) , HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(CustomNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage() , HttpStatus.NOT_FOUND.value()) , HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(CustomRuntimeException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR.value()) , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }



    @ExceptionHandler(DatabaseInternalServerError.class)
    public ResponseEntity<ErrorResponse> handleDatabaseRuntimeExceptions(DatabaseInternalServerError ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR.value()) , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DatabasePersistenceException.class)
    public ResponseEntity<ErrorResponse> handleDatabasePersistenceExceptions(DatabasePersistenceException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR.value()) , HttpStatus.INTERNAL_SERVER_ERROR  );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage() , HttpStatus.CONFLICT.value()) , HttpStatus.CONFLICT  );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ValidationErrorResponse("Validation Failed", errors), HttpStatus.BAD_REQUEST);
    }


}
