package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cart")
@AllArgsConstructor
@Data
public class Cart extends BaseEntity {

    @Column(name = "total_price" , nullable = false)
    private double totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    @OneToOne(cascade = {CascadeType.DETACH , CascadeType.MERGE , CascadeType.REFRESH , CascadeType.PERSIST})
    @JoinColumn(name = "customer_id")
    private User customer;

    public void addCartItems(CartItem cartItem){
        items.add(cartItem);
    }

}
