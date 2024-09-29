package com.example.ecommerce_app.Dto.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RemoveFromCartDto {


    private long productId;

    private long customerId;


}
