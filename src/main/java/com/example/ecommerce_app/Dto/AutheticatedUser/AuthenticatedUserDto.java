package com.example.ecommerce_app.Dto.AutheticatedUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticatedUserDto {
    String email;
    long id;
    String token;
}
