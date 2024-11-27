package com.example.ecommerce_app.Dto.RecentlyViewedProducts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecentlyViewedProductsDeleteDto {

    private long productId;

    private long userId;
}
