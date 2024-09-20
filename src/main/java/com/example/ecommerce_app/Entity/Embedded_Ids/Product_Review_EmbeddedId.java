package com.example.ecommerce_app.Entity.Embedded_Ids;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
public class Product_Review_EmbeddedId {

    private Long customerId;

    private Long productId;

    public Product_Review_EmbeddedId(Long vendorId, Long productId) {
        this.customerId = vendorId;
        this.productId = productId;
    }

}
