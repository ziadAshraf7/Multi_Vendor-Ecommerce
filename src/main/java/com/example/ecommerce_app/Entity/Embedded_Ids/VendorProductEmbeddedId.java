package com.example.ecommerce_app.Entity.Embedded_Ids;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class VendorProductEmbeddedId implements Serializable {


    private Long vendorId;

    private Long productId;

    public VendorProductEmbeddedId(Long vendorId, Long productId) {
        this.vendorId = vendorId;
        this.productId = productId;
    }
}
