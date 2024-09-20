package com.example.ecommerce_app.Services.User;


import com.example.ecommerce_app.Dto.User.UserDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Mapper.UserMapper;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {


  private UserRepository userRepository;

  private UserMapper userMapper;


  public void addUser(UserDto userDto){
      try {
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
      }catch (RuntimeException e){

      }
  }

  public User getUserEntity(long userId , UserRoles userRole){
     Optional<User>  userOptional = userRepository.findById(userId);

     if(!userOptional.isPresent()) throw new RuntimeException("user is not found");

     User user = userOptional.get();

     if(user.getUserRole() != userRole) throw new RuntimeException("user is not a " + userRole );

     return user;
  }



}
