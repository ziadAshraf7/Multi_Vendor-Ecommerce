package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.Authentication.LoginDto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Services.Authentication.AuthenticationService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/public")
@AllArgsConstructor
public class PublicController {

    private final ProductService productService;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@ModelAttribute @Valid LoginDto loginDto){
        String jwtToken = authenticationService.loginWithJwt(loginDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        return new ResponseEntity<>("login Successfully", headers, HttpStatus.OK);

    }

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@ModelAttribute
                                             @Valid UserCreationDto userCreationDto){

        userService.addUser(userCreationDto);
        return new ResponseEntity<>("Account Created Successfully " , HttpStatus.CREATED);
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<Product_Detailed_Dto> getProduct(@PathVariable long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/products/category")
    public ResponseEntity<Page<Product_Overview_Dto>> getProductsByCategoryId(@Param("categoryId") long categoryId){
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId));
    }

    @GetMapping("/products/brand")
    public ResponseEntity<Page<Product_Overview_Dto>> getProductsByBrandId(@Param("brandId") long brandId){
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId));
    }

    @GetMapping("/products/bestseller/category")
    public ResponseEntity<Page<Product_Overview_Dto>> getBestSellerProductsPerCategory(
            @Param("categoryId") long categoryId){
        return ResponseEntity.ok(productService.getBestSellerProductsPerCategory(categoryId));
    }

    @GetMapping("/products/bestseller/brand")
    public ResponseEntity<Page<Product_Overview_Dto>> getBestSellerProductsPerBrand(
            @Param("brandId") long brandId){
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId));
    }

    @GetMapping("/products/mostViews/category")
    public ResponseEntity<Page<Product_Overview_Dto>> getMostViewsProductsPerCategory(
            @Param("categoryId") long categoryId){
        return ResponseEntity.ok(productService.getBestSellerProductsPerCategory(categoryId));
    }

    @GetMapping("/products/mostViews/brand")
    public ResponseEntity<Page<Product_Overview_Dto>> getMostViewsProductsPerBrand(
            @Param("brandId") long brandId){
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId));
    }

}
