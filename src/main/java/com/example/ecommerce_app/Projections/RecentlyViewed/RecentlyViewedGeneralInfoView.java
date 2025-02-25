package com.example.ecommerce_app.Projections.RecentlyViewed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecentlyViewedGeneralInfoView {

    private Long productId;

    private String productName;

    private String thumbNailPath;

    private String title;

}
