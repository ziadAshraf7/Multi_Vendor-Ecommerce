package com.example.ecommerce_app.Entity;


import com.example.ecommerce_app.Entity.Embedded_Ids.OrderItemEmbeddedId;
import com.example.ecommerce_app.Entity.Enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@Table(name = "order_item" , indexes = {
        @Index(name = "idx_unique_customer_order",
                columnList = "customer_id, order_id",
                unique = true)
})
@SuperBuilder
public class OrderItem extends BaseEntity{


    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id" , nullable = false)
    private User vendor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status" , columnDefinition = "ENUM('PENDING', 'DELIVERED', 'CANCELLED', 'SHIPPED') DEFAULT 'PENDING'")
    private OrderItemStatus status ;

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
