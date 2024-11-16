package com.example.ecommerce_app.Repositery.CartItem;

import com.example.ecommerce_app.Dto.CartItem.CartItemWithProductIdDTO;
import com.example.ecommerce_app.Entity.CartItem;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItem_EmbeddedId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem , CartItem_EmbeddedId> {



    @Query("SELECT c FROM CartItem c WHERE c.id.cartId = :customerId ")
    Page<CartItem> findByCartId(@Param("customerId") long customerId , Pageable pageable);

    @Query("SELECT new com.example.ecommerce_app.Dto.CartItem.CartItemWithProductIdDTO(c.quantity, p.id) " +
            "FROM CartItem c  JOIN c.product p " +
            "WHERE c.id.cartId = :customerId")
    Page<CartItemWithProductIdDTO> findCartItemsWithProductIdByCartId(@Param("customerId") long customerId , Pageable pageable);


    @Query("SELECT new com.example.ecommerce_app.Dto.CartItem.CartItemWithProductIdDTO(c.quantity, p.id) " +
            "FROM CartItem c  JOIN c.product p " +
            "WHERE c.id.cartId = :customerId")
    List<CartItemWithProductIdDTO> findCartItemsWithProductIdByCartId(@Param("customerId") long customerId);


    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM CartItem c WHERE c.id.productId = :productId")
    boolean existsByProductId(long productId);

    @Query("SELECT c FROM CartItem c WHERE c.id.cartId = :customerId " +
            "AND c.id.productId = :productId AND c.id.vendorProductId = :vendorProductId")
    CartItem findByProductIdAndCartIdAndVendorProductId(
            @Param("productId") long productId ,
            @Param("customerId") long customerId ,
            @Param("vendorProductId") long vendorProductId);

    @Query("DELETE FROM CartItem e WHERE e.cart.id = :customerId AND e.vendorProduct.product.id = :productId ")
    @Modifying
    @Transactional
    void deleteByCustomerIdAndProductId(@Param("customerId") long customerId ,@Param("productId") long productId);

    @Query("DELETE FROM CartItem e WHERE e.cart.id = :customerId ")
    @Modifying
    @Transactional
    void deleteAllBYCustomerId(@Param("customerId") long customerId);
}
