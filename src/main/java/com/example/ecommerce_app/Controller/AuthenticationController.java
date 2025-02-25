package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.Authentication.LoginDto;
import com.example.ecommerce_app.Dto.Authentication.SuccessfulLoginInfo;
import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Services.AnonymousUserServices.AnonymousUserCart.AnonymousCartService;
import com.example.ecommerce_app.Services.Authentication.AuthenticationService;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
import com.example.ecommerce_app.Services.NotificationService.Services.NotificationService;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Services.linkingUserCartsManagement.linkingUserCartsManagementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.example.ecommerce_app.Utills.AuthenticationUtils.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final CartService cartService;

    private final com.example.ecommerce_app.Services.linkingUserCartsManagement.linkingUserCartsManagementService linkingUserCartsManagementService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<SuccessfulLoginInfo> loginUser(@ModelAttribute @Valid LoginDto loginDto , HttpSession httpSession){

        SuccessfulLoginInfo successfulLoginInfo = authenticationService.loginWithJwt(loginDto);

        linkingUserCartsManagementService.linkBetweenSessionCartAndUserCart(httpSession.getId() , successfulLoginInfo.getUserId());

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER , "Bearer " + successfulLoginInfo.getJwtToken());

        return new ResponseEntity<>(successfulLoginInfo, headers, HttpStatus.OK);
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> signUpUser(@ModelAttribute
                                             @Valid UserCreationDto userCreationDto){
        User customer = userService.addCustomerUser(userCreationDto);
        cartService.createUserCart(customer);
        return new ResponseEntity<>("Account Created Successfully " , HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
        authenticationService.logOut();
//       response.addCookie(authorizationCookie);
        return "LogOut Successfully";
    }


}
