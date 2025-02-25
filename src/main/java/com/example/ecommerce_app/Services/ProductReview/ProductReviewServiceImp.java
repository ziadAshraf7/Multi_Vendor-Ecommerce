package com.example.ecommerce_app.Services.ProductReview;

import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReviewCreationDto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Update_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.ProductReview;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Mapper.ProductReview_Mapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.ProductReview.ProductReviewRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            throw new CustomNotFoundException("Cannot retrieve Product Reviews for product id " + productId);
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
            throw new CustomNotFoundException("Cannot retrieve User Reviews for user id " + UserId);
        }

    }

    @Override
    @Transactional
    public void addReview(ProductReviewCreationDto productReviewCreationDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = ( (AuthenticatedUserDto) authentication.getPrincipal()).getId();
        if(productReviewRepository.getReviewsCountPerUser(
                productReviewCreationDto.getProductId() ,
                userId
        ) > 0) throw new CustomBadRequestException("User cannot have multiple reviews per product ");

        boolean isProductExists = productRepository.existsById(productReviewCreationDto.getProductId());

        if(!isProductExists) throw new CustomNotFoundException("Product is not found");

        Product product = productRepository.getReferenceById(productReviewCreationDto.getProductId());

        boolean isUserExists = userRepository.existsById(userId);
        if(!isUserExists) throw new CustomNotFoundException("User is not Found");
        User user = userRepository.getReferenceById(userId);

        ProductReview productReview = productReviewMapper.fromCreationDtoToEntity(productReviewCreationDto);


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
    public void removeReview(long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = ( (AuthenticatedUserDto) authentication.getPrincipal()).getId();
        try {
            productReviewRepository.deleteProductReview(productId , userId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to Remove review ");
        }
    }

    @Override
    @Transactional
    public ProductReview_Detailed_Dto updateReview(ProductReview_Update_Dto productReview_update_dto)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long userId = ( (AuthenticatedUserDto) authentication.getPrincipal()).getId();

        ProductReview productReview = productReviewRepository.findByProductIdAndUserId(
                    productReview_update_dto.getProductId() , userId);

        if(productReview == null) throw new CustomNotFoundException("user review is not found");
        if(productReview_update_dto.getRate() != null) productReview.setRate(productReview_update_dto.getRate());
        if(productReview_update_dto.getDescription() != null) productReview.setDescription(productReview_update_dto.getDescription());
        if(productReview_update_dto.getImage() != null) productReview.setImageFileName(productReview_update_dto.getImage().getOriginalFilename());

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

        }catch (CustomRuntimeException e){
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

        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to retrieve reviews");
        }
    }


}
