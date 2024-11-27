package com.example.ecommerce_app.Services.RecentlyViewedProducts;

import com.example.ecommerce_app.Dto.RecentlyViewedProducts.RecentlyViewedProductsDeleteDto;
import com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView;

import java.util.List;

public interface RecentlyViewedService {

    void deleteRecentlyViewedProduct(RecentlyViewedProductsDeleteDto recentlyViewedProductsDeleteDto);

    List<RecentlyViewedGeneralInfoView> getAllRecentlyViewedByUser(long userId);

}
