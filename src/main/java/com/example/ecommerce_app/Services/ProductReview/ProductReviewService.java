package com.example.ecommerce_app.Services.ProductReview;

import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Creation_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Update_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Update_Dto;

import java.io.IOException;
import java.util.List;

public interface ProductReviewService {

    List<ProductReview_Detailed_Dto> getAllReviewsPerProduct(long productId);

    List<ProductReview_Detailed_Dto> getAllReviewsPerUser(long UserId);

    void addReview(ProductReview_Creation_Dto productReview_creation_dto);

    void removeReview(long userId , long productId);

    ProductReview_Detailed_Dto updateReview(ProductReview_Update_Dto productReview_update_dto) throws IOException;

    List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingASC(long productId , long UserId);

    List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingDESC(long productId , long UserId);


}
