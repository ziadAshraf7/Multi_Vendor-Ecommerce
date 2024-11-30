package com.example.ecommerce_app.Entity.Embedded_Ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CartItemEmbeddedId implements Serializable {
    private Long cartId;

    private Long productId;

    private Long vendorProductId;
}
