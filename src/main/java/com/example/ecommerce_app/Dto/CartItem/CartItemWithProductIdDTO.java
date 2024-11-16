package com.example.ecommerce_app.Dto.CartItem;

import com.example.ecommerce_app.Entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartItemWithProductIdDTO {

    private int quantity;

    private Long  productId;

    public CartItemWithProductIdDTO(int quantity, Long productId) {
        this.quantity = quantity;
        this.productId = productId;
    }
}
