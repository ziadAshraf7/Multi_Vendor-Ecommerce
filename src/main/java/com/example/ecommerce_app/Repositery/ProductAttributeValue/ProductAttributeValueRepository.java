package com.example.ecommerce_app.Repositery.ProductAttributeValue;

import com.example.ecommerce_app.Entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue , Long> {

    @Transactional
    @Modifying
    @Query(
      value = "DELETE FROM product_attribute_value WHERE product_id = :productId AND attribute_id = :attributeId AND id = :valueId",
      nativeQuery = true
    )
    void deleteByProductIdAndAttributeIdAndValueId(
                                                 @Param("productId") long productId ,
                                                 @Param("attributeId") long attributeId ,
                                                 @Param("valueId") long value);

    @Query(value = "SELECT  FROM product_attribute_value  where product_id = :productId" ,
    nativeQuery = true)
    List<ProductAttributeValue> findByProductId(@Param("productId") long productId);
}
