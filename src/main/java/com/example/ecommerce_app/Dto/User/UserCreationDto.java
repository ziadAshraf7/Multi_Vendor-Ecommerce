package com.example.ecommerce_app.Dto.User;

import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserCreationDto {

    @NotEmpty
    @NotNull
    private String userName;

    @NotEmpty
    @NotNull
    @Email
    private String email;


    @NotEmpty
    @NotNull
    private String password;


    @NotEmpty
    @NotNull
    private String phoneNumber;


    @NotEmpty
    @NotNull
    private String address;

}
