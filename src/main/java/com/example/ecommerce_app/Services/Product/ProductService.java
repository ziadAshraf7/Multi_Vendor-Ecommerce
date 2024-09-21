package com.example.ecommerce_app.Services.Product;

import com.example.ecommerce_app.Dto.Product_Table.ProductFilterDto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Update_Dto;
import com.example.ecommerce_app.Entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

     void removeProduct(long productId);

     Product_Detailed_Dto getProductById(long productId);

     Product_Detailed_Dto getProductByName(String name);

     Product_Overview_Dto updateProduct(Product_Update_Dto product_update_dto);

     void updateProductRating(int user_rate , Product product);

     Page<Product_Overview_Dto> getProductsByCategoryId(long categoryId);

     Page<Product_Overview_Dto> getNewArrivalProducts(long categoryId);

     Page<Product_Overview_Dto> getDiscountProducts(long categoryId);

     Page<Product_Overview_Dto> getBestSellerProductsPerCategory(long categoryId );

     Page<Product_Overview_Dto> filterProducts(ProductFilterDto productFilterDto);

     Product getProductEntityById(long productId);
}
