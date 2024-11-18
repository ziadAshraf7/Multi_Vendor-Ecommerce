package com.example.ecommerce_app.Entity;

import com.example.ecommerce_app.Entity.Embedded_Ids.VendorProductEmbeddedId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@Table(name = "Vendor_Product" , indexes = {
        @Index(name = "IDX_VendorId_VendorProduct",
                columnList = "vendor_id , product_id , price , stock , discount, created_at"
                , unique = false) ,
        @Index(name = "IDX_ProductId_VendorProduct",
                columnList = "product_id ,vendor_id, price , stock , discount, created_at"
                , unique = false) ,
        @Index(name = "Idx_Unique_product_vendor_constraint",
                columnList = "product_id, vendor_id",
                unique = true)
})
public class VendorProduct extends BaseEntity {

    public VendorProduct(){
        super();
    }

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne( fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "vendorProduct", cascade = CascadeType.REMOVE )
    private List<CartItem> cartItems;
}
