package com.example.ecommerce_app.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@SuperBuilder
@Table(name = "`order`" ,
        indexes = {
        @Index(name = "idx_customer_created_status",
                columnList = "customer_id,total_amount , status, created_at, updated_at"
               , unique = false)
})
public class Order extends BaseEntity {

    public Order(){super();}

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST , CascadeType.REMOVE} )
    private List<OrderItem> orderItems;

}
