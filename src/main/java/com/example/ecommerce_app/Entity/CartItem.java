package com.example.ecommerce_app.Entity;

import com.example.ecommerce_app.Entity.Embedded_Ids.CartItem_EmbeddedId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item")
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CartItem {

    @EmbeddedId
    private CartItem_EmbeddedId id;

    @Column(name = "quantity" , nullable = false)
    private int quantity;

    @Column(name = "price" , nullable = false)
    private double price;

    @Column(name = "stock" , nullable = false)
    private int stock;

    @Column(name = "discount" , nullable = false)
    private double discount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY , cascade = {CascadeType.PERSIST})
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

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
