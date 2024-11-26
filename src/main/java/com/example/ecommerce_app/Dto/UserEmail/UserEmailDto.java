package com.example.ecommerce_app.Dto.UserEmail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserEmailDto {

    @NotNull(message = "email cannot be null")
    private  String userEmail;

}
