package com.example.ecommerce_app.Repositery.ProductReview;


import com.example.ecommerce_app.Entity.Embedded_Ids.Product_Review_EmbeddedId;
import com.example.ecommerce_app.Entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview , Product_Review_EmbeddedId> {

    List<ProductReview> findAllByProductId(long productId);

    List<ProductReview> findAllByUserId(long userId);

    @Query(value = "SELECT * FROM product_review pr where pr.product_id = :productId AND pr.customer_id = :userId "
            , nativeQuery = true)
    ProductReview findByProductIdAndUserId(@Param("productId") long productId ,@Param("userId") long userId);

    @Query(value = "SELECT * FROM product_review pr where pr.product_id = :productId AND pr.customer_id = :userId ORDER BY pr.rate ASC "
            , nativeQuery = true)
    List<ProductReview> getProductReviewsSortedByRatingASC (@Param("productId") long productId ,@Param("userId") long userId);

    @Query(value = "SELECT * FROM product_review pr where pr.product_id = :productId AND pr.customer_id = :userId ORDER BY pr.rate DESC "
            , nativeQuery = true)
    List<ProductReview> getProductReviewsSortedByRatingDESC (@Param("productId") long productId ,@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from product_review pr where pr.product_id = :productId AND pr.customer_id = :userId " , nativeQuery = true)
    void deleteProductReview(@Param("productId") long productId ,@Param("userId") long userId);
}
