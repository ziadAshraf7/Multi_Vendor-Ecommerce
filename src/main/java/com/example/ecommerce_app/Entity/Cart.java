package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cart")
@AllArgsConstructor
@Data
@SuperBuilder

public class Cart  extends BaseEntity {

    public Cart(){
        super();
    }

    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    private List<CartItem> items;

    @ManyToOne(  fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "created_at" , updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at" , insertable = false)
    private LocalDateTime updatedAt;

    public void addCartItems(CartItem cartItem){
        items.add(cartItem);
    }

}
