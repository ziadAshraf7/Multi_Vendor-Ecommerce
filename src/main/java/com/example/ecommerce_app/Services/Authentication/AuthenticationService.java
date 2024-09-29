package com.example.ecommerce_app.Services.Authentication;

import com.example.ecommerce_app.Dto.Authentication.LoginDto;
import com.example.ecommerce_app.Dto.Authentication.SuccessfulLoginInfo;
import jakarta.servlet.http.Cookie;

public interface AuthenticationService {

   SuccessfulLoginInfo loginWithJwt(LoginDto loginDto);

   Cookie logOut();
}
