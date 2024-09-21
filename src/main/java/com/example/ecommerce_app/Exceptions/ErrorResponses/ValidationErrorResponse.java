package com.example.ecommerce_app.Exceptions.ErrorResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private Map<String , String> errorDetails;
}
