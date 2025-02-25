package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.User.UserInfoDetails;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/generalInfo")
    public ResponseEntity<UserInfoDetails> getUserInfo(@RequestParam String email){

        return ResponseEntity.ok(userService.getUserByEmail(email));

    }


}
