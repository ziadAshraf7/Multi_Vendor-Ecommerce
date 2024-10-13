package com.example.ecommerce_app.Entity.Embedded_Ids;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemEmbeddedId {

    private long productId;

    private long orderId;
}
