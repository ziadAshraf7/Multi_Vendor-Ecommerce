package com.example.ecommerce_app.Mapper;

import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemResponseDto;
import com.example.ecommerce_app.Entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {


//   CartItem fromCreationDtoToEntity(CartItemCreationDto cartItemCreationDto);


   @Mapping(target = "thumbNail" , expression = "java(cartItem.getProduct().getThumbNail())")
   @Mapping(target = "productName" , expression = "java(cartItem.getProduct().getName())")
   @Mapping(target = "title" , expression = "java(cartItem.getProduct().getTitle())")
   @Mapping(target = "price" , expression = "java(cartItem.getVendorProduct().getPrice())")
   @Mapping(target = "productId" , expression = "java(cartItem.getProduct().getId())")
   @Mapping(target = "vendorProductId" , expression = "java(cartItem.getId().getVendorProductId())")
   CartItemResponseDto fromEntityToResponseDto(CartItem cartItem);

}
