package com.example.ecommerce_app.Services.Authentication;

import com.example.ecommerce_app.Dto.Authentication.LoginDto;
import com.example.ecommerce_app.Dto.Authentication.SuccessfulLoginInfo;
import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomAuthorizationException;
import com.example.ecommerce_app.Services.JWT.JwtService;
import com.example.ecommerce_app.Services.User.UserService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.ecommerce_app.Utills.AuthenticationUtils.AUTHORIZATION_HEADER;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService{

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public SuccessfulLoginInfo loginWithJwt(LoginDto loginDto) {
        User user = userService.getUserEntityByEmail(loginDto.getEmail());

        if(user == null) throw new CustomNotFoundException("User is not Exists");

        if(!passwordEncoder.matches(loginDto.getPassword() , user.getPassword())){
            throw new CustomAuthorizationException("Password is incorrect");
        }

        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user.getEmail() , user.getId());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUserDto  ,
                user.getPassword() ,
                List.of(new SimpleGrantedAuthority(user.getUserRole().toString()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return SuccessfulLoginInfo.builder()
                .jwtToken(jwtService.generateToken(loginDto.getEmail()))
                .userId(user.getId())
                .build();
    }

    @Override
    public Cookie logOut() {
        try {
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, null);
            cookie.setMaxAge(0);
            return cookie;
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Error While Logout User");
        }

    }

}
