package com.example.ecommerce_app.Entity;


import com.example.ecommerce_app.Entity.Embedded_Ids.Product_Review_EmbeddedId;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_review")
@Data
@Builder
@AllArgsConstructor
public class ProductReview  {


    public ProductReview() {
    }

    @EmbeddedId
    private Product_Review_EmbeddedId id;

    @Column(name = "rate" , nullable = false)
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private double rate;

    @Column(name = "description" , columnDefinition = "TEXT",  nullable = false )
    @NotEmpty
    private String description;

    @Column(name = "image" )
    private String imageFileName;

    @ManyToOne(fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "customer_id")
    @MapsId("customerId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    @MapsId("productId")
    private Product product;

    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at" , insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
