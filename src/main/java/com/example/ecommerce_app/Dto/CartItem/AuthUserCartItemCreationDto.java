package com.example.ecommerce_app.Dto.CartItem;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@SuperBuilder
public class AuthUserCartItemCreationDto extends CartItemDto {

    public AuthUserCartItemCreationDto(){
        super();
    }

    private long customerId;

}
