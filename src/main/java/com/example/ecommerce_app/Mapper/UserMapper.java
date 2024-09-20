package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.User.UserDto;
import com.example.ecommerce_app.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toDto(User user);

}
