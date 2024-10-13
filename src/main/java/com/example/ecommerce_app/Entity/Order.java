package com.example.ecommerce_app.Entity;

import com.example.ecommerce_app.Entity.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@Table(name = "order" ,
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST , CascadeType.REMOVE} )
    private List<OrderItem> orderItems;

}
