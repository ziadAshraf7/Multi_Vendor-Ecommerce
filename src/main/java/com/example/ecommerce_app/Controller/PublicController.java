package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.Authentication.LoginDto;
import com.example.ecommerce_app.Dto.Authentication.SuccessfulLoginInfo;
import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Dto.User.UserCreationDto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Services.Authentication.AuthenticationService;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Cart_Management.Cart_Management_Service;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Session.AnonymousUser.AnonymousCartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ecommerce_app.Utills.AuthenticationUtils.AUTHORIZATION_HEADER;


@RestController
@RequestMapping("api/public")
@AllArgsConstructor
public class PublicController {

    private final ProductService productService;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final CartService cartService;

    private final AnonymousCartService anonymousCartService;

    private final Cart_Management_Service cartManagementService;


    @PostMapping("/login")
    @Transactional
    public ResponseEntity<String> loginUser(@ModelAttribute @Valid LoginDto loginDto , HttpSession httpSession){

        SuccessfulLoginInfo successfulLoginInfo = authenticationService.loginWithJwt(loginDto);

        cartManagementService.link_between_sessionCart_userCart(httpSession.getId() , successfulLoginInfo.getUserId());

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER , "Bearer " + successfulLoginInfo.getJwtToken());

        return new ResponseEntity<>("login Successfully", headers, HttpStatus.OK);

    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> signUpUser(@ModelAttribute
                                             @Valid UserCreationDto userCreationDto){
        User customer = userService.addUser(userCreationDto);
        cartService.createUserCart(customer);
        return new ResponseEntity<>("Account Created Successfully " , HttpStatus.CREATED);
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
       Cookie authorizationCookie = authenticationService.logOut();
       response.addCookie(authorizationCookie);
       return "LogOut Successfully";
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

    @PostMapping("/cart")
    public void addToCart(@RequestBody CartItemDto cartItemDto , HttpSession httpSession){
        anonymousCartService.addToCart(cartItemDto , httpSession.getId());
    }

    @GetMapping("/cart")
    public List<CartItemDto> getCartItems(HttpSession httpSession){
      return  anonymousCartService.getCartItemsBySessionId(httpSession.getId());
    }
}
