package com.example.ecommerce_app.Entity;


import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendor_product_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vendor_Product_Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(name = "image" , columnDefinition = "LONGBLOB" , nullable = false)
    private byte[] image;

    @ManyToOne(cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.REFRESH , CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.REFRESH , CascadeType.PERSIST})
    @JoinColumn(name = "vendor_id")
    private User vendor;

}
