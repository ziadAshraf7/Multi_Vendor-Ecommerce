package com.example.ecommerce_app.Entity.Embedded_Ids;

import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class Vendor_Product_EmbeddedId implements Serializable {


    private Long vendorId;

    private Long productId;

    public Vendor_Product_EmbeddedId(Long vendorId, Long productId) {
        this.vendorId = vendorId;
        this.productId = productId;
    }
}
