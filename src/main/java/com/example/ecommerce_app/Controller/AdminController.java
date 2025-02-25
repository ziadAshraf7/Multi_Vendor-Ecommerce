package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/user/admin")
    public ResponseEntity<String> createAdminUser(UserCreationDto userCreationDto){
        userService.addAdminUser(userCreationDto);
        return new ResponseEntity<>("Created successfully" , HttpStatus.CREATED);
    }

    @PostMapping("/user/vendor")
    public ResponseEntity<String> createVendorUser(UserCreationDto userCreationDto){
        userService.addVendorUser(userCreationDto);
        return new ResponseEntity<>("Created successfully" , HttpStatus.CREATED);
    }

}
