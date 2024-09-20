package com.example.ecommerce_app.Dto.User;


import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {


    private String userName;

    private String email;

    private String phoneNumber;

    private UserRoles userRole;

    private String address;


}
