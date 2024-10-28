package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email" , expression = "java(userCreationDto.getEmail().toLowerCase())")
    User toEntity(UserCreationDto userCreationDto);

    UserInfoDetails toUserInfoDetails(User user);

}
