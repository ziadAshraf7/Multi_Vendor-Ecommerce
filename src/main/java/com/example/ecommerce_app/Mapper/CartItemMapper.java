package com.example.ecommerce_app.Mapper;

import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {


//   CartItem fromCreationDtoToEntity(CartItemCreationDto cartItemCreationDto);


   @Mapping(target = "thumbNail" , expression = "java(cartItem.getProduct().getThumbNail())")
   @Mapping(target = "rating" , expression = "java(cartItem.getProduct().getRating())")
   @Mapping(target = "name" , expression = "java(cartItem.getProduct().getName())")
   @Mapping(target = "title" , expression = "java(cartItem.getProduct().getTitle())")
   CartItemDto fromEntityToResponseDto(CartItem cartItem);

}
