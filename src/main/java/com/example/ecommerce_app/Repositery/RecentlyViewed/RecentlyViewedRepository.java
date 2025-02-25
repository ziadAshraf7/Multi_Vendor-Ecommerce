package com.example.ecommerce_app.Repositery.RecentlyViewed;

import com.example.ecommerce_app.Entity.Embedded_Ids.RecentlyViewedEmbeddedId;
import com.example.ecommerce_app.Entity.RecentlyViewed;
import com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed , RecentlyViewedEmbeddedId> {

    @Query("""
      SELECT new com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView(rv.product.id , rv.product.name , rv.product.thumbNail , rv.product.title)
      FROM RecentlyViewed rv
      WHERE rv.user.id = :userId
    """)
    List<RecentlyViewedGeneralInfoView> findByUserId(
            @Param("userId") long userId);

    @Query("""
      SELECT new com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView(rv.product.id , rv.product.name , rv.product.thumbNail , rv.product.title)
      FROM RecentlyViewed rv
      WHERE rv.user.id = :userId And rv.product.id = :productId
    """)
    RecentlyViewedGeneralInfoView findByUserIdAndProductId(
            @Param("userId") long userId , @Param("productId") long productId);

    @Query("SELECT count(rv) FROM RecentlyViewed rv WHERE rv.user.id = :userId And rv.product.id = :productId")
    int getCount(@Param("userId") long userId , @Param("productId") long productId);

    @Query(value = "DELETE From recently_viewed rv WHERE rv.user_id =:userId And rv.product_id =:productId" ,
    nativeQuery = true)
    @Modifying
    @Transactional
    void deleteRecentlyViewedEntity(@Param("userId") long userId , @Param("productId") long productId);
}
