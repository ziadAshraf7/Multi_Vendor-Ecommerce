package com.example.ecommerce_app.Services.ProductReview;

import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Creation_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Update_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.ProductReview;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Mapper.ProductReview_Mapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.ProductReview.ProductReviewRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Data
public class ProductReviewServiceImp implements ProductReviewService{

    private final ProductReviewRepository productReviewRepository;

    private final ProductReview_Mapper productReviewMapper;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public List<ProductReview_Detailed_Dto> getAllReviewsPerProduct(long productId) {
        List<ProductReview> productReviews = productReviewRepository.findAllByProductId(productId);

        List<ProductReview_Detailed_Dto> productReview_detailed_dtos = new ArrayList<>(productReviews.size());

        productReviews.forEach((pr) -> productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(pr)));

        return productReview_detailed_dtos;

    }

    @Override
    public List<ProductReview_Detailed_Dto> getAllReviewsPerUser(long UserId) {
        List<ProductReview> productReviews = productReviewRepository.findAllByUserId(UserId);

        List<ProductReview_Detailed_Dto> productReview_detailed_dtos = new ArrayList<>(productReviews.size());

        productReviews.forEach((pr) -> productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(pr)));

        return productReview_detailed_dtos;    }

    @Override
    public void addReview(ProductReview_Creation_Dto productReview_creation_dto) {

        Product product = productRepository.findById(productReview_creation_dto.getProductId())
                .orElseThrow(() -> new RuntimeException("product is not found"));

        User user = userRepository.findById(productReview_creation_dto.getUserId())
                .orElseThrow(() -> new RuntimeException("user is not found"));

        ProductReview productReview = productReviewMapper.fromCreationDtoToEntity(productReview_creation_dto);

        productReview.setProduct(product);
        productReview.setUser(user);

        try {
            productReviewRepository.save(productReview);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void removeReview(long userId, long productId) {
        try {
            productReviewRepository.deleteProductReview(productId , userId);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public ProductReview_Detailed_Dto updateReview(ProductReview_Update_Dto productReview_update_dto) throws IOException {

        ProductReview productReview = productReviewRepository.findByProductIdAndUserId(
                productReview_update_dto.getProductId() ,
                productReview_update_dto.getUserId()
        );

        if(productReview_update_dto.getRate() != null) productReview.setRate(productReview_update_dto.getRate());
        if(productReview_update_dto.getDescription() != null) productReview.setDescription(productReview_update_dto.getDescription());
        if(productReview_update_dto.getImage() != null) productReview.setImage(productReview_update_dto.getImage().getBytes());


        try {
            productReviewRepository.save(productReview);
            return productReviewMapper.to_ProductReview_Detailed_Dto(productReview);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingASC(long productId, long UserId) {
        try {
            List<ProductReview> productReviewList = productReviewRepository.getProductReviewsSortedByRatingASC(productId , UserId);

            List<ProductReview_Detailed_Dto>  productReview_detailed_dtos = new ArrayList<>(productReviewList.size());

            for(ProductReview productReview : productReviewList) productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(productReview));

            return productReview_detailed_dtos;

        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingDESC(long productId, long UserId) {
        try {
            List<ProductReview> productReviewList = productReviewRepository.getProductReviewsSortedByRatingDESC(productId , UserId);

            List<ProductReview_Detailed_Dto>  productReview_detailed_dtos = new ArrayList<>(productReviewList.size());

            for(ProductReview productReview : productReviewList) productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(productReview));

            return productReview_detailed_dtos;

        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }


}
