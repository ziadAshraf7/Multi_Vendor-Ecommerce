package com.example.ecommerce_app.Entity;


import com.example.ecommerce_app.Entity.Embedded_Ids.OrderItemEmbeddedId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Data
@Table(name = "order_item" , indexes = {
        @Index(name = "idx_customer_price_quantity",
                columnList = "customer_id, price, quantity, created_at, updated_at"
                , unique = false) ,
        @Index(name = "idx_unique_customer_order",
                columnList = "customer_id, order_id",
                unique = true)
})
@Builder
public class OrderItem {

    @EmbeddedId
    private  OrderItemEmbeddedId orderItemEmbeddedId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    @MapsId(value = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private User customer;

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
