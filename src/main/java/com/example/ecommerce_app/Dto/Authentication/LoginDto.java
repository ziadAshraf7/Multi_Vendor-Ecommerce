package com.example.ecommerce_app.Dto.Authentication;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginDto {

    @NotEmpty
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    private String password;

}
