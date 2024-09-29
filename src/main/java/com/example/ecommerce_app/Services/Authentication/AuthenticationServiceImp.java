package com.example.ecommerce_app.Services.Authentication;

import com.example.ecommerce_app.Dto.Authentication.LoginDto;
import com.example.ecommerce_app.Dto.Authentication.SuccessfulLoginInfo;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotAuhthorized;
import com.example.ecommerce_app.Services.JWT.JwtService;
import com.example.ecommerce_app.Services.User.UserService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
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

        if(!passwordEncoder.matches(loginDto.getPassword() , user.getPassword())){
            throw new NotAuhthorized("Password is incorrect");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail() ,
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
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Error While Logout User");
        }

    }

}
