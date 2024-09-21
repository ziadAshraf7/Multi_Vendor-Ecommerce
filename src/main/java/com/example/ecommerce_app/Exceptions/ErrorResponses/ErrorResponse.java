package com.example.ecommerce_app.Exceptions.ErrorResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private int statusCode;
}
