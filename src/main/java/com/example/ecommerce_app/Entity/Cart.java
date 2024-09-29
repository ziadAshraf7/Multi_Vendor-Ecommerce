package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cart")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class Cart   {

    @Id
    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "total_price" , nullable = false)
    private double totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    @OneToOne(cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.REFRESH , CascadeType.PERSIST})
    @MapsId
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


    public void addCartItems(CartItem cartItem){
        items.add(cartItem);
    }

}
