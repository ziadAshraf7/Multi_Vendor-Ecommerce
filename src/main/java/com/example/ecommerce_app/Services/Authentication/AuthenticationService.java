package com.example.ecommerce_app.Services.Authentication;

import com.example.ecommerce_app.Dto.Authentication.LoginDto;

public interface AuthenticationService {

   String loginWithJwt(LoginDto loginDto);

}
