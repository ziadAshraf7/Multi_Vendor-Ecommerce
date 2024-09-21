package com.example.ecommerce_app.Services.User;


import com.example.ecommerce_app.Dto.User.UserDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Mapper.UserMapper;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {


  private UserRepository userRepository;

  private UserMapper userMapper;

  public void addUser(UserDto userDto){
      try {
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
      }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add User");
      }
  }

  public User getUserEntityById(long userId , UserRoles userRole){
    User  user = userRepository.findById(userId)
             .orElseThrow(() -> new NotFoundException("user is not found"));

     if(user.getUserRole() != userRole) throw new RuntimeException("user is not a " + userRole );

     return user;
  }

    @Override
    public User getUserEntityById(long userId) {
      return   userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }


}
