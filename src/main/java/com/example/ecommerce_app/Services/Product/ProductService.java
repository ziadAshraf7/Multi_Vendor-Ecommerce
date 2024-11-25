package com.example.ecommerce_app.Services.Product;

import com.example.ecommerce_app.Dto.Product_Table.ProductDetailedDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductFilterDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductOverviewDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductUpdateDto;
import com.example.ecommerce_app.Entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

     void removeProduct(long productId);

     ProductDetailedDto getProductById(long productId);

     ProductDetailedDto getProductByName(String name);

     ProductOverviewDto updateProduct(ProductUpdateDto product_update_dto);

     void updateProductRating(int user_rate , Product product);

     Page<ProductOverviewDto> getProductsByCategoryId(long categoryId);

     Page<ProductOverviewDto> getNewArrivalProducts(long categoryId);

     Page<ProductOverviewDto> getDiscountProducts(long categoryId);

     Page<ProductOverviewDto> getBestSellerProductsPerCategory(long categoryId );

     Page<ProductOverviewDto> filterProducts(ProductFilterDto productFilterDto);

     Product getProductEntityById(long productId);

     Page<ProductOverviewDto> getProductsPerBrand(long brandId);

     Page<ProductOverviewDto>  getBestSellerProductsPerBrand(long brandId);

}
