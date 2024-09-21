package com.example.ecommerce_app.Services.User;

import com.example.ecommerce_app.Dto.User.UserDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;

public interface UserService {


     void addUser(UserDto userDto);

     User getUserEntityById(long userId , UserRoles userRole);

     User getUserEntityById(long userId);


    }
