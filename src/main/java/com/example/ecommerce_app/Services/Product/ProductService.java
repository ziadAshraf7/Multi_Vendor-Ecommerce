package com.example.ecommerce_app.Services.Product;

import com.example.ecommerce_app.Dto.Product_Table.ProductDetailedDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductFilterDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductOverviewDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductUpdateDto;
import com.example.ecommerce_app.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

     void removeProduct(long productId);

     ProductOverviewDto updateProduct(ProductUpdateDto product_update_dto);

     void updateProductRating(int user_rate , Product product);

     Page<ProductOverviewDto> getProductsByCategoryId(long categoryId , Pageable pageable);

     Page<ProductOverviewDto> getNewArrivalProducts(long categoryId , Pageable pageable);

     Page<ProductOverviewDto> getDiscountProducts(long categoryId , Pageable pageable);

     Page<ProductOverviewDto> filterProducts(ProductFilterDto productFilterDto , Pageable pageable);

     Product getProductEntityById(long productId);

     Page<ProductOverviewDto> getProductsPerBrand(long brandId , Pageable pageable);

     Page<ProductOverviewDto>  getBestSellerProductsPerBrand(long brandId , Pageable pageable);

     Page<ProductOverviewDto> getBestSellerProductsPerCategory(long categoryId , Pageable pageable );

     Page<ProductOverviewDto>  getMostViewedProductsPerBrand(long brandId , Pageable pageable);

     Page<ProductOverviewDto> getMostViewedProductsPerCategory(long categoryId , Pageable pageable );

}
