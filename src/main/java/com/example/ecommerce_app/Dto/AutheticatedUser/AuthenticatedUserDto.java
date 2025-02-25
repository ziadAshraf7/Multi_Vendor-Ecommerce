package com.example.ecommerce_app.Dto.AutheticatedUser;

import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticatedUserDto {
    String role;
    String email;
    long id;
    String token;
}
