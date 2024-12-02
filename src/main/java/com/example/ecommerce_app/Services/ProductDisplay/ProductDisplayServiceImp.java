package com.example.ecommerce_app.Services.ProductDisplay;

import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductDetailedDto;
import com.example.ecommerce_app.Entity.Embedded_Ids.RecentlyViewedEmbeddedId;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.RecentlyViewed;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Mapper.ProductMapper;
import com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView;
import com.example.ecommerce_app.Projections.User.UserGeneralInfoInfoView;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.RecentlyViewed.RecentlyViewedRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Data
public class ProductDisplayServiceImp implements ProductDisplayService{

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final RecentlyViewedRepository recentlyViewedRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProductDetailedDto getProduct(long productId) {
        Product product = productRepository.getEagerProductEntity(productId);

        if(product == null ) throw new CustomNotFoundException("product is not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() != "anonymousUser"){
            String userEmail =  ((AuthenticatedUserDto) authentication).getEmail();
            UserGeneralInfoInfoView userGeneralInfoInfoView = userRepository.findGeneralInfoByEmail(userEmail);
            if(userGeneralInfoInfoView != null)  displayProduct(productId , userGeneralInfoInfoView.getId());
        }
        product.setViewsCount(product.getViewsCount() + 1);
        productRepository.save(product);
        return productMapper.toProductDetailedDto(product);
    }

    @Transactional
    private void displayProduct(long productId , long userId){
        RecentlyViewedGeneralInfoView recentlyViewedGeneralInfoView = recentlyViewedRepository.findByUserIdAndProductId(userId , productId);

        if(recentlyViewedGeneralInfoView == null){
            User user = userRepository.getReferenceById(userId);
            Product product = productRepository.getReferenceById(productId);
            RecentlyViewed recentlyViewed =  RecentlyViewed.builder().product(product).user(user)
                    .recentlyViewedEmbeddedId(new RecentlyViewedEmbeddedId(productId , userId)).build();
            recentlyViewedRepository.save(recentlyViewed);

            return;
        }

        RecentlyViewed recentlyViewed = recentlyViewedRepository.getReferenceById(
                new RecentlyViewedEmbeddedId(productId , userId)
        );

        recentlyViewed.setViewedAt(LocalDateTime.now());

        recentlyViewedRepository.save(recentlyViewed);
    }

}
