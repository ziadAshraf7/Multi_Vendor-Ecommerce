package com.example.ecommerce_app.Services.RecentlyViewedProducts;

import com.example.ecommerce_app.Dto.RecentlyViewedProducts.RecentlyViewedProductsDeleteDto;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView;
import com.example.ecommerce_app.Repositery.RecentlyViewed.RecentlyViewedRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class RecentlyViewedServiceImp implements RecentlyViewedService{

    private final RecentlyViewedRepository recentlyViewedRepository;

    @Transactional
    @Override
    public void deleteRecentlyViewedProduct(RecentlyViewedProductsDeleteDto
                                                        recentlyViewedProductsDeleteDto) {
        int entityCount = recentlyViewedRepository.getCount(
                recentlyViewedProductsDeleteDto.getUserId(),
                recentlyViewedProductsDeleteDto.getProductId()
        );

        if(entityCount == 0) throw new CustomNotFoundException("entity is not found");

        try {
            recentlyViewedRepository.deleteRecentlyViewedEntity(
                    recentlyViewedProductsDeleteDto.getUserId(),
                    recentlyViewedProductsDeleteDto.getProductId()
            );
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database error while deleting viewed product");
        }

    }


    @Transactional(readOnly = true)
    @Override
    public List<RecentlyViewedGeneralInfoView> getAllRecentlyViewedByUser(long userId) {
        return recentlyViewedRepository.findByUserId(userId);
    }
}
