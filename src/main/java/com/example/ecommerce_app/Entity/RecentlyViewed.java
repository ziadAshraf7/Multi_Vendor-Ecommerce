package com.example.ecommerce_app.Entity;

import com.example.ecommerce_app.Entity.Embedded_Ids.RecentlyViewedEmbeddedId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "recently_viewed")
public class RecentlyViewed {

    @EmbeddedId
    private RecentlyViewedEmbeddedId recentlyViewedEmbeddedId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @Column(name = "viewed_at" , insertable = false)
    private LocalDateTime viewedAt;

}
