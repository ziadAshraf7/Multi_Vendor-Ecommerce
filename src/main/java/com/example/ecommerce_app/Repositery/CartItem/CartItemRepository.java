package com.example.ecommerce_app.Repositery.CartItem;

import com.example.ecommerce_app.Entity.CartItem;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItem_EmbeddedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem , CartItem_EmbeddedId> {


    void deleteByProductId(long productId);

    @Query("SELECT c FROM CartItem c WHERE c.id.cartId = :customerId ")
    List<CartItem> findByCartId(@Param("customerId") long customerId);

    boolean existsByProductId(long productId);

    @Query("SELECT c FROM CartItem c WHERE c.id.cartId = :customerId AND c.id.productId = :productId")
    CartItem findByProductIdAndCartId(@Param("productId") long productId , @Param("customerId") long customerId);

    @Query("DELETE FROM CartItem e WHERE e.cart.id = :customerId AND e.product.id = :productId ")
    @Modifying
    @Transactional
    void deleteByCustomerIdAndProductId(@Param("customerId") long customerId ,@Param("productId") long productId);

    @Query("DELETE FROM CartItem e WHERE e.cart.id = :customerId ")
    @Modifying
    @Transactional
    void deleteALLbYCustomerId(@Param("customerId") long customerId);
}
