package com.example.ecommerce_app.Services.User;

import com.example.ecommerce_app.Dto.User.UserDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;

import java.util.Optional;

public interface UserService {


     void addUser(UserDto userDto);

     User getUserEntity(long userId , UserRoles userRole);


    }
