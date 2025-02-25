package com.example.ecommerce_app.Services.ProductReview;

import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReviewCreationDto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Update_Dto;

import java.io.IOException;
import java.util.List;

public interface ProductReviewService {

    List<ProductReview_Detailed_Dto> getAllReviewsPerProduct(long productId);

    List<ProductReview_Detailed_Dto> getAllReviewsPerUser(long UserId);

    void addReview(ProductReviewCreationDto productReview_creation_dto);

    void removeReview(long productId);

    ProductReview_Detailed_Dto updateReview(ProductReview_Update_Dto productReview_update_dto) throws IOException;

    List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingASC(long productId , long UserId);

    List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingDESC(long productId , long UserId);

}
