package com.example.ecommerce_app.Services;

import com.example.ecommerce_app.Dto.CartItem.AuthUserCartItemCreationDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemQuantityDto;
import com.example.ecommerce_app.Dto.CartItem.RemoveFromCartDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItemEmbeddedId;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Services.AnonymousUserServices.AnonymousUserCart.AnonymousCartService;
import com.example.ecommerce_app.Redis.Session.SessionManagement.SessionService;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.CartItem.CartItemServiceImp;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.Vendor_Product.VendorProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CartManagementServiceTest {

    @Mock
    private VendorProductRepository vendorProductRepository;

    @Mock
    private  CartItemRepository cartItemRepository;

    @Mock
    private  CartService cartService;

    @Mock
    private  ProductService productService;

    @Mock
    private VendorProductService vendor_product_service;

    @Mock
    private  CartItemMapper cartItemMapper;

    @Mock
    private  AnonymousCartService anonymousCartService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private CartItemServiceImp cartItemService;

    private Product product;


    @BeforeEach
    void init(){
        product = Product.builder().id(1l).build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void CartManagementServiceModifyCartItemQuantityReturnsVoid(){
        CartItemQuantityDto cartItemQuantityDto = CartItemQuantityDto
                .builder().quantity(3).vendorProductId(1L).customerId(1L).productId(1L).build();

        Cart cart = Cart.builder().customerId(1L).build();
        VendorProduct vendorProduct = VendorProduct.builder().id(1L).stock(10).build();
        CartItem cartItem = CartItem.builder()
                .id(new CartItemEmbeddedId(1L , 1l, 1l))
                .product(product)
                .cart(cart)
                .vendorProduct(vendorProduct)
                .quantity(2).build();

        when(vendorProductRepository
                .findById(1L))
                .thenReturn(Optional.of(vendorProduct));

        when(cartItemRepository
                .findByProductIdAndCartIdAndVendorProductId(1L , 1L , 1L))
                .thenReturn(cartItem);

        when(cartService.getCartByCustomerId(1L)).thenReturn(cart);


        assertDoesNotThrow(() -> {
            cartItemService.modifyCartItemQuantity(cartItemQuantityDto);
        });

        verify(cartItemRepository , times(1)).save(any());

    }

    @Test
    void CartManagementServiceAddToCartReturnsVoid(){
        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto
                .builder().productId(1L).vendorProductId(1L).customerId(1L).quantity(1).build();

        Cart cart = Cart.builder().customerId(1L).build();
        VendorProduct vendor_product =
                VendorProduct.builder().stock(2).price(20).build();
        Product product = Product.builder().vendor_products(List.of(vendor_product)).id(1L).build();

        when(cartService.getCartByCustomerId(1L)).thenReturn(cart);
        when(productService.getProductEntityById(1L)).thenReturn(product);
        when(vendor_product_service.getByVendorIdAndProductId(1L)).thenReturn(vendor_product);


        assertDoesNotThrow(() -> cartItemService.addToCart(authUserCartItemCreationDto));

        assertThat(authUserCartItemCreationDto.getQuantity()).isNotNegative();
        assertThat(vendor_product.getStock()).isGreaterThanOrEqualTo(authUserCartItemCreationDto.getQuantity());
        assertThat(vendor_product.getStock()).isNotNegative();
        assertThat(vendor_product.getStock()).isNotEqualTo(0);

        verify(cartItemRepository , times(1)).save(any());

    }



    @Test
    void CartManagementServiceRemoveFromCartReturnsVoid(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L)
                .productId(1L)
                .build();

        Cart cart = Cart.builder().customerId(1L).build();

        CartItem cartItem = CartItem.builder().id(new CartItemEmbeddedId()).build();

        when(cartService.getCartByCustomerId(1L)).thenReturn(cart);
        when(cartItemRepository.findByProductIdAndCartIdAndVendorProductId(1L,1L , 1L)).thenReturn(cartItem);

        assertDoesNotThrow(() -> {
            cartItemService.removeFromCart(removeFromCartDto);
        });

        verify(cartService , times(1)).persistCartEntity(any());
        verify(cartItemRepository , times(1)).delete(any());
    }


    @Test
    void CartManagementServiceRemoveAllFromCartReturnsVoid(){
        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto
                .builder()
                .productId(1L)
                .customerId(1L)
                .build();
        Cart cart = Cart.builder().customerId(1L).build();
        when(cartService.getCartByCustomerId(1L)).thenReturn(cart);

        assertDoesNotThrow(() -> {
            cartItemService.removeAllFromCart(1L);
        });

        verify(cartService,times(1)).persistCartEntity(any());
        verify(cartItemRepository, times(1)).deleteAllBYCartId(1L);
    }


//    @Test
//    void CartManagementServiceLinkBetweenSessionCartUserCartReturnsVoid(){
//       String sessionId = "abc";
//       long vendorId = 10L;
//       long customerId = 1L;
//       long vendorProductId = 10L;
//       long sessionProductId1 = 1L;
//       long sessionProductId2 = 2L;
//       long userCartProductId= sessionProductId1;
//       Cart cart = Cart.builder().customerId(customerId)
//               .customer(User.builder().
//                       userRole(UserRoles.ROLE_CUSTOMER).id(customerId).build()).build();
//       CartItemDto redisCartItem1 = CartItemDto.builder().vendorProductId(vendorProductId).productId(sessionProductId1).build();
//       CartItemDto redisCartItem2 = CartItemDto.builder().vendorProductId(vendorProductId).productId(sessionProductId2).build();
//       User vendor = User.builder().id(vendorId).userRole(UserRoles.ROLE_VENDOR).build();
//       Product productToBeAddedAsCartItem = Product.builder().id(sessionProductId2).build();
//       VendorProduct vendorProduct = VendorProduct.builder()
//               .price(20).product(productToBeAddedAsCartItem).vendor(vendor).stock(50).build();
//
//       CartItem cartItemToBeAddedToDatabase = CartItem.builder()
//                .cart(cart)
//                .product(productToBeAddedAsCartItem)
//                .quantity(20)
//                .id(new CartItem_EmbeddedId(customerId , 1L , 1L))
//                .build();
//
//       CartItem userCartItem = CartItem.builder()
//                .id(new CartItem_EmbeddedId())
//                .product(Product.builder()
//                        .id(userCartProductId)
//                        .build()).build();
//
//       List<CartItemDto> sessionCartItems = new ArrayList<>(List.of(
//               redisCartItem1 ,
//               redisCartItem2
//       ));
//
//       List<CartItem> userCartItems = new ArrayList<>(List.
//                of(userCartItem));
//
//        when(anonymousCartService.getCartItemsBySessionId(anyString())).thenReturn(sessionCartItems);
//        when(cartItemRepository.findByCartId(anyLong())).thenReturn(userCartItems);
//        when(cartService.getCartByCustomerId(anyLong())).thenReturn(cart);
//        when(productService.getProductEntityById(sessionProductId2)).thenReturn(productToBeAddedAsCartItem);
//        when(vendor_product_service.getByVendorIdAndProductId(anyLong() ,anyLong() )).thenReturn(vendorProduct);
//        when(cartItemRepository.save(any())).thenReturn(cartItemToBeAddedToDatabase);
//
//        cartManagementService.linkBetweenSessionCartAndUserCart(sessionId , customerId);
//
//
//
//
//        verify(sessionService , times(1)).deleteSession(anyString());
//
//    }


    @Test
    @DisplayName("cart item not found exception when updating cart item quantity")
    void CartManagementServiceModifyCartItemQuantityThrowsNotFoundException(){

        CartItemQuantityDto cartItemQuantityDto = CartItemQuantityDto.builder().productId(1L).customerId(1L).vendorProductId(1L).build();

        when(cartItemRepository.findByProductIdAndCartIdAndVendorProductId(1L , 1L , 1L)).thenThrow(new CustomNotFoundException("Unable to find cart item"));

        assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.modifyCartItemQuantity(cartItemQuantityDto);
        });

        verify(cartItemRepository , never()).save(any());

    }


    @Test
    @DisplayName("database exception while trying to save the cart item after updating the quantity")
    void CartManagementServiceModifyCartItemQuantityThrowsDatabasePersistenceException(){
        Cart cart = Cart.builder().customerId(1L).build();

        CartItem cartItem = CartItem.builder()
                .cart(cart).id(new CartItemEmbeddedId())
                .quantity(10).build();

        CartItemQuantityDto cartItemQuantityDto = CartItemQuantityDto.builder()
                .productId(1L).customerId(1L)
                .vendorProductId(1L).build();

        when(cartItemRepository.findByProductIdAndCartIdAndVendorProductId(1L , 1L , 1L)).thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenThrow(new DatabasePersistenceException("unable to update cart item "));
        when(cartService.getCartByCustomerId(1L)).thenReturn(cart);

        DatabasePersistenceException exception = assertThrows(DatabasePersistenceException.class , () -> {
            cartItemService.modifyCartItemQuantity(cartItemQuantityDto);
        });

        assertEquals(exception.getMessage() , "unable to update cart item ");
        verify(cartItemRepository , times(1)).save(cartItem);
    }

    @Test
    @DisplayName("addToCart when cart is not found exception")
    void CartManagementServiceAddToCartThrowsCustomNotFoundExceptionWhenCartIsNotFound(){

        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto.builder().customerId(1L).build();

        when(cartService.getCartByCustomerId(1l)).thenThrow(new CustomNotFoundException("customer user is not found for user id 1"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.addToCart(authUserCartItemCreationDto);
        });

        assertEquals(exception.getMessage() , "customer user is not found for user id 1");
        verify(cartItemRepository , never()).save(any());

    }

    @Test
    @DisplayName("addToCart when product is not found exception")
    void CartManagementServiceAddToCartThrowsCustomNotFoundExceptionWhenProductIsNotFound(){

        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto.builder().productId(1L).customerId(1L).build();

        when(cartService.getCartByCustomerId(1l)).thenReturn(Cart.builder().customerId(1L).build());
        when(productService.getProductEntityById(1l)).thenThrow(new CustomNotFoundException("Unable to find Product with Id 1"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.addToCart(authUserCartItemCreationDto);
        });

        assertEquals(exception.getMessage() , "Unable to find Product with Id 1");
        verify(cartItemRepository , never()).save(any());

    }


    @Test
    @DisplayName("addToCart when vendorProduct is not found exception")
    void CartManagementServiceAddToCartThrowsCustomNotFoundExceptionWhenVendorProductIsNotFound(){

        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto.builder().vendorProductId(1L).productId(1L).customerId(1L).build();

        when(cartService.getCartByCustomerId(1l)).thenReturn(Cart.builder().customerId(1L).build());
        when(productService.getProductEntityById(1l)).thenReturn(Product.builder().id(1L).build());
        when(vendor_product_service.getByVendorIdAndProductId(1l)).thenThrow(new CustomNotFoundException("vendorProduct entity cannot be found"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.addToCart(authUserCartItemCreationDto);
        });

        assertEquals(exception.getMessage() , "vendorProduct entity cannot be found");
        verify(cartItemRepository , never()).save(any());

    }

    @Test
    @DisplayName("addToCart when product is ran out of stock ")
    void CartManagementServiceAddToCartThrowsCustomBadRequestExceptionWhenProductOutOfStock(){

        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto.builder()
                .vendorProductId(1L).productId(1L).quantity(10).customerId(1L).build();

        when(cartService.getCartByCustomerId(1l)).thenReturn(Cart.builder().customerId(1L).build());
        when(productService.getProductEntityById(1l)).thenReturn(Product.builder().id(1L).build());
        when(vendor_product_service.getByVendorIdAndProductId(1l))
                .thenReturn(VendorProduct.builder().stock(0).discount(0).build());

        CustomBadRequestException exception =  assertThrows(CustomBadRequestException.class , () -> {
            cartItemService.addToCart(authUserCartItemCreationDto);
        });

        assertEquals(exception.getMessage() , "product is ran out of stock");
        verify(cartItemRepository , never()).save(any());

    }


    @Test
    @DisplayName("addToCart when product stock is less than the cart item quantity ")
    void CartManagementServiceAddToCartThrowsCustomBadRequestExceptionWhenCartItemQuantityExcess(){

        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto.builder()
                .vendorProductId(1L).productId(1L).quantity(10).customerId(1L).build();

        when(cartService.getCartByCustomerId(1l)).thenReturn(Cart.builder().customerId(1L).build());
        when(productService.getProductEntityById(1l)).thenReturn(Product.builder().id(1L).build());
        when(vendor_product_service.getByVendorIdAndProductId(1l))
                .thenReturn(VendorProduct.builder().stock(5).discount(0).build());

        CustomBadRequestException exception =  assertThrows(CustomBadRequestException.class , () -> {
            cartItemService.addToCart(authUserCartItemCreationDto);
        });

        assertEquals(exception.getMessage() , "quantity selected is greater than product stock");
        verify(cartItemRepository , never()).save(any());

    }

    @Test
    @DisplayName("database failed while persisting cart item  ")
    void CartManagementServiceAddToCartThrowsDatabasePersistenceException(){

        AuthUserCartItemCreationDto authUserCartItemCreationDto = AuthUserCartItemCreationDto.builder()
                .vendorProductId(1L).productId(1L).quantity(10).customerId(1L).build();

        when(cartService.getCartByCustomerId(1l)).thenReturn(Cart.builder().customerId(1L).build());
        when(productService.getProductEntityById(1l)).thenReturn(Product.builder().id(1L).build());
        when(vendor_product_service.getByVendorIdAndProductId(1l))
                .thenReturn(VendorProduct.builder().stock(20).discount(0).build());

        when(cartItemRepository.save(any())).thenThrow(new DatabasePersistenceException("Unable to add product to the cart"));

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class , () -> {
            cartItemService.addToCart(authUserCartItemCreationDto);
        });

        assertEquals(exception.getMessage() , "Unable to add product to the cart");
        verify(cartItemRepository , times(1)).save(any());

    }


    @Test
    @DisplayName("remove from cart when cart is not found exception")
    void CartManagementServiceRemoveFromCartThrowsCustomNotFoundExceptionWhenCartIsNotFound(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1l))
                .thenThrow(new CustomNotFoundException("customer cart user is not found for user id 1"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.removeFromCart(removeFromCartDto);
        });

        assertEquals(exception.getMessage() , "customer cart user is not found for user id 1");
        verify(cartService , never()).persistCartEntity(any());
        verify(cartItemRepository , never()).delete(any());

    }

    @Test
    @DisplayName("remove from cart when cart item is not found exception")
    void CartManagementServiceRemoveFromCartThrowsCustomNotFoundExceptionWhenCartItemIsNotFound(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1l))
                .thenReturn(Cart.builder().customerId(1L).build() );

        when(cartItemRepository.findByProductIdAndCartIdAndVendorProductId(1L , 1L , 1L))
                .thenThrow(new CustomNotFoundException("unable to find cart item"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.removeFromCart(removeFromCartDto);
        });

        assertEquals(exception.getMessage() , "unable to find cart item");
        verify(cartService , never()).persistCartEntity(any());
        verify(cartItemRepository , never()).delete(any());

    }

    @Test
    @DisplayName("database failed while updating cart")
    void CartManagementServiceRemoveFromCartThrowsDatabasePersistenceExceptionWhileUpdatingCart(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1l))
                .thenReturn(Cart.builder().customerId(1L).build() );

        when(cartItemRepository.findByProductIdAndCartIdAndVendorProductId(1l , 1L , 1L))
                .thenReturn(CartItem.builder().quantity(10).build());

        when(cartService.persistCartEntity(any(Cart.class))).thenThrow(
                new DatabasePersistenceException("Unable to delete product from cart")
        );

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class , () -> {
            cartItemService.removeFromCart(removeFromCartDto);
        });

        assertEquals(exception.getMessage() , "Unable to delete product from cart");
        verify(cartService).persistCartEntity(any());
        verify(cartItemRepository , never()).delete(any());
    }

    @Test
    @DisplayName("database failed deleting cart item")
    void CartManagementServiceRemoveFromCartThrowsDatabasePersistenceExceptionWhileDeletingCartItem(){

        CartItem cartItem = CartItem.builder().quantity(10).build();

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1l))
                .thenReturn(Cart.builder().customerId(1L).build() );

        when(cartItemRepository.findByProductIdAndCartIdAndVendorProductId(1L ,1l , 1L))
                .thenReturn(cartItem);


        doThrow(new DatabasePersistenceException("Unable to delete product from cart"))
                .when(cartItemRepository).delete(any(CartItem.class));

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class , () -> {
            cartItemService.removeFromCart(removeFromCartDto);
        });

        assertEquals(exception.getMessage() , "Unable to delete product from cart");
        verify(cartService).persistCartEntity(any());
        verify(cartItemRepository ).delete(any());
    }

    @Test
    @DisplayName("remove all from cart when cart is not found exception")
    void CartManagementServiceRemoveAllFromCartThrowsCustomNotFoundExceptionWhenCartIsNotFound(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1l))
                .thenThrow(new CustomNotFoundException("customer cart user is not found for user id 1"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
            cartItemService.removeAllFromCart(1L);
        });

        assertEquals(exception.getMessage() , "customer cart user is not found for user id 1");
        verify(cartService , never()).persistCartEntity(any());
        verify(cartItemRepository , never()).deleteAllBYCartId(anyLong());

    }

    @Test
    @DisplayName("database failed while updating cart")
    void CartManagementServiceRemoveAllFromCartThrowsDatabasePersistenceExceptionWhileUpdatingCart(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1L)).thenReturn(Cart.builder().build());

        when(cartService.persistCartEntity(Cart.builder().build()))
                .thenThrow(new DatabasePersistenceException("Unable to remove all Products from Cart"));


        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class , () -> {
            cartItemService.removeAllFromCart(1L);
        });

        assertEquals(exception.getMessage() , "Unable to remove all Products from Cart");
        verify(cartService).persistCartEntity(any());
        verify(cartItemRepository , never())
                .deleteAllBYCartId(anyLong());
    }

    @Test
    @DisplayName("database failed deleting cart item")
    void CartManagementServiceRemoveAllFromCartThrowsDatabasePersistenceExceptionWhileDeletingAllCartItem(){

        RemoveFromCartDto removeFromCartDto = RemoveFromCartDto.builder()
                .customerId(1L).productId(1L).build();

        when(cartService.getCartByCustomerId(1L)).thenReturn(Cart.builder().build());


        doThrow(new DatabasePersistenceException("Unable to remove all Products from Cart"))
                .when(cartItemRepository).deleteAllBYCartId(1L);

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class , () -> {
            cartItemService.removeAllFromCart(1L);
        });

        assertEquals(exception.getMessage() , "Unable to remove all Products from Cart");
        verify(cartService).persistCartEntity(any());
        verify(cartItemRepository )
                .deleteAllBYCartId(anyLong());
    }


//    @Test
//    @DisplayName("linking between anonymous user cart and authenticated user cart exception when user cart is not found")
//    void CartManagementServiceLinkSessionsThrowsCustomNotFoundException(){
//
//        when(cartItemRepository.findByCartId(1L)).thenThrow(
//                new CustomNotFoundException("Cannot get Cart for customer id " + 1)
//        );
//
//        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
//            cartItemService.linkBetweenSessionCartAndUserCart("abc" , 1L);
//        });
//
//
//        assertEquals(exception.getMessage() , "Cannot get Cart for customer id 1");
//        verify(sessionService , never()).deleteSession(anyString());
//
//    }

}
