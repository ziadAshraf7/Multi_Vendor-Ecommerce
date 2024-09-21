package com.example.ecommerce_app.Services.ProductReview;

import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Creation_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Update_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.ProductReview;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Mapper.ProductReview_Mapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.ProductReview.ProductReviewRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class ProductReviewServiceImp implements ProductReviewService{

    private final ProductReviewRepository productReviewRepository;

    private final ProductReview_Mapper productReviewMapper;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ProductService productService;

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview_Detailed_Dto> getAllReviewsPerProduct(long productId) {
        try {
            List<ProductReview> productReviews = productReviewRepository.findAllByProductId(productId);

            List<ProductReview_Detailed_Dto> productReview_detailed_dtos = new ArrayList<>(productReviews.size());

            productReviews.forEach((pr) -> productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(pr)));

            return productReview_detailed_dtos;
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new NotFoundException("Cannot retrieve Product Reviews for product id " + productId);
        }


    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview_Detailed_Dto> getAllReviewsPerUser(long UserId) {
        try {
            List<ProductReview> productReviews = productReviewRepository.findAllByUserId(UserId);

            List<ProductReview_Detailed_Dto> productReview_detailed_dtos = new ArrayList<>(productReviews.size());

            productReviews.forEach((pr) -> productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(pr)));

            return productReview_detailed_dtos;
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new NotFoundException("Cannot retrieve User Reviews for user id " + UserId);
        }

    }

    @Override
    @Transactional
    public void addReview(ProductReview_Creation_Dto productReview_creation_dto) {

        Product product = productService.getProductEntityById(productReview_creation_dto.getProductId());

        User user = userService.getUserEntityById(productReview_creation_dto.getUserId());

        ProductReview productReview = productReviewMapper.fromCreationDtoToEntity(productReview_creation_dto);

        productReview.setProduct(product);
        productReview.setUser(user);

        try {
            productReviewRepository.save(productReview);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add review");
        }

    }

    @Override
    @Transactional
    public void removeReview(long userId, long productId) {
        try {
            productReviewRepository.deleteProductReview(productId , userId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to Remove review ");
        }
    }

    @Override
    @Transactional
    public ProductReview_Detailed_Dto updateReview(ProductReview_Update_Dto productReview_update_dto) throws IOException {
        ProductReview productReview;
        try {
             productReview = productReviewRepository.findByProductIdAndUserId(
                    productReview_update_dto.getProductId() ,
                    productReview_update_dto.getUserId()
            );
          }catch (RuntimeException e){
                log.error(e.getMessage());
                throw new NotFoundException("unable to find the review");
        }

        if(productReview_update_dto.getRate() != null) productReview.setRate(productReview_update_dto.getRate());
        if(productReview_update_dto.getDescription() != null) productReview.setDescription(productReview_update_dto.getDescription());
        if(productReview_update_dto.getImage() != null) productReview.setImage(productReview_update_dto.getImage().getBytes());

        try {
            productReviewRepository.save(productReview);
            return productReviewMapper.to_ProductReview_Detailed_Dto(productReview);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to Update the Review");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingASC(long productId, long UserId) {
        try {
            List<ProductReview> productReviewList = productReviewRepository.getProductReviewsSortedByRatingASC(productId , UserId);

            List<ProductReview_Detailed_Dto>  productReview_detailed_dtos = new ArrayList<>(productReviewList.size());

            for(ProductReview productReview : productReviewList) productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(productReview));

            return productReview_detailed_dtos;

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to retrieve reviews");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReview_Detailed_Dto> getProductReviewsSortedByRatingDESC(long productId, long UserId) {
        try {
            List<ProductReview> productReviewList = productReviewRepository.getProductReviewsSortedByRatingDESC(productId , UserId);

            List<ProductReview_Detailed_Dto>  productReview_detailed_dtos = new ArrayList<>(productReviewList.size());

            for(ProductReview productReview : productReviewList) productReview_detailed_dtos.add(productReviewMapper.to_ProductReview_Detailed_Dto(productReview));

            return productReview_detailed_dtos;

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to retrieve reviews");
        }
    }


}
