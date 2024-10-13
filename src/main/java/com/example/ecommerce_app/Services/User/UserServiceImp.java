package com.example.ecommerce_app.Services.User;


import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Mapper.UserMapper;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {


  private UserRepository userRepository;

  private UserMapper userMapper;

  private PasswordEncoder passwordEncoder;

  @Override
  public User addUser(UserCreationDto userCreationDto){
      try {
        User user = userMapper.toEntity(userCreationDto);
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));
        return  userRepository.save(user);
      }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add User");
      }
  }

  public User getUserEntityById(long userId , UserRoles userRole){
     try {
        return userRepository.getReferenceById(userId);
     }catch (RuntimeException e){
         log.error(e.getMessage());
         throw new NotFoundException("user is not found");
     }

  }

    @Override
    public User getUserEntityById(long userId) {
      User user = userRepository.getReferenceById(userId);
      if(user == null) throw new NotFoundException("user is not found for user id" + userId);
      return user;
    }

    @Override
    public UserInfoDetails getUserByEmail(String Email) {
      try {
          User user = userRepository.findByEmail(Email);
          return userMapper.toUserInfoDetails(user);
      }catch (RuntimeException e){
          log.error(e.getMessage());
          throw new CustomRuntimeException("User Email" + Email + "is not Found");
      }
    }

    @Override
    public User getUserEntityByEmail(String email) {
        try {
           return userRepository.findByEmail(email);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("User Email" + email + "is not Found");
        }    }

}
