package com.example.ecommerce_app.Services.User;


import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
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

    private User getUserByPhoneNumber(String phoneNumber){

        return userRepository.findByPhoneNumber(phoneNumber);
    }


    @Transactional
    @Override
    public User addAdminUser(UserCreationDto userCreationDto) {

            User existingUserWithPhoneNumber =  getUserByPhoneNumber(userCreationDto.getPhoneNumber());
            User existingUserWithEmail = getUserEntityByEmail(userCreationDto.getEmail());
            if(existingUserWithEmail != null) throw new CustomConflictException("Email is Already exists");
            if(existingUserWithPhoneNumber != null) throw new CustomConflictException("Phone Number is Already exists");

            User user = userMapper.toEntity(userCreationDto);
            user.setUserRole(UserRoles.ROLE_ADMIN);
            user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

            try {
                return  userRepository.save(user);

            }catch (DatabasePersistenceException e){
                log.error(e.getMessage());
                throw new DatabasePersistenceException("Database Error While Persisting User Entity");
            }

    }


    @Transactional
    @Override
    public User addVendorUser(UserCreationDto userCreationDto) {
            User existingUserWithPhoneNumber =  getUserByPhoneNumber(userCreationDto.getPhoneNumber());
            User existingUserWithEmail = getUserEntityByEmail(userCreationDto.getEmail());
            if(existingUserWithEmail != null) throw new CustomConflictException("Email is Already exists");
            if(existingUserWithPhoneNumber != null) throw new CustomConflictException("Phone Number is Already exists");

            User user = userMapper.toEntity(userCreationDto);
            user.setUserRole(UserRoles.ROLE_VENDOR);
            user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

        try {
            return  userRepository.save(user);

        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database Error While Persisting User Entity");
        }

    }

    @Transactional
    @Override
    public User addCustomerUser(UserCreationDto userCreationDto) {
            User existingUserWithPhoneNumber =  getUserByPhoneNumber(userCreationDto.getPhoneNumber());
            User existingUserWithEmail = getUserEntityByEmail(userCreationDto.getEmail());
            if(existingUserWithEmail != null) throw new CustomConflictException("Email is Already exists");
            if(existingUserWithPhoneNumber != null) throw new CustomConflictException("Phone Number is Already exists");

            User user = userMapper.toEntity(userCreationDto);
            user.setUserRole(UserRoles.ROLE_CUSTOMER);
            user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

        try {
            return  userRepository.save(user);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database Error While Persisting User Entity");
        }

    }

    public User getUserEntityById(long userId , UserRoles userRole){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("user is not found for user id " + userId));
        if(user.getUserRole() != userRole) throw new CustomConflictException("User is not " + userRole);
        return user;
   }

    @Override
    public User getUserEntityById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("user is not found for user id " + userId));
    }

    @Override
    public UserInfoDetails getUserByEmail(String Email) {
          User user = userRepository.findByEmail(Email);
          if(user == null) throw new CustomNotFoundException("Cannot find Email " + Email);
          return userMapper.toUserInfoDetails(user);
    }

    @Override
    public User getUserEntityByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) throw new CustomNotFoundException("Cannot find Email " + email);
        return user;
    }

}
