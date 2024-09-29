package com.example.ecommerce_app.Services.User;

import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;

public interface UserService {

     User addUser(UserCreationDto userCreationDto);

     User getUserEntityById(long userId , UserRoles userRole);

     User getUserEntityById(long userId);

     UserInfoDetails getUserByEmail(String Email);

     User getUserEntityByEmail(String email);

    }
