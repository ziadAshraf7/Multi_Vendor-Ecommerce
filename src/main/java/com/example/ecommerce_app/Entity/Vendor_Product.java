package com.example.ecommerce_app.Entity;

import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.util.Date;

@Entity
@Table(name = "Vendor_Product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vendor_Product {

    @EmbeddedId
    private Vendor_Product_EmbeddedId id ;

    @ManyToOne(  cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.REFRESH , CascadeType.PERSIST})
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne( cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.REFRESH , CascadeType.PERSIST})
    @MapsId("vendorId")
    @JoinColumn(name = "vendor_id")
    private User vendor;

    @Column(name = "stock" , nullable = false)
    @Min(value = 0)
    private int stock;

    @Column(name = "price"  , nullable = false)
    @DecimalMin(value = "0.1")
    private double price;

    @Column(name = "discount" , nullable = false)
    @DecimalMax(value = "100.0")
    @DecimalMin(value = "0.0")
    private double discount;

    @Column(name = "created_at" , updatable = false)
    private Date createdAt;

    @Column(name = "updated_at" , insertable = false)
    private Date updatedAt;


}
