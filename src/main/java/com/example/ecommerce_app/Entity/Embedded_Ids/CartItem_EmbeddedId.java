package com.example.ecommerce_app.Entity.Embedded_Ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem_EmbeddedId {
    private long cartId;

    private long productId;

    private long vendorProductId;
}
