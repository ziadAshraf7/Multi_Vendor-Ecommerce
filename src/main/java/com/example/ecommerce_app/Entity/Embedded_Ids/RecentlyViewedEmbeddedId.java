package com.example.ecommerce_app.Entity.Embedded_Ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@AllArgsConstructor
@Data
public class RecentlyViewedEmbeddedId {

    private long productId;

    private long userId;
}
