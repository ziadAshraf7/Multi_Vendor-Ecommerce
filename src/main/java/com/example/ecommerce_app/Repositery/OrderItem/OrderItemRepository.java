package com.example.ecommerce_app.Repositery.OrderItem;

import com.example.ecommerce_app.Entity.OrderItem;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem , Long> {

    Page<OrderItem> findByVendorId(long vendorId , Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.product.id = :productId")
    void deleteByOrderIdAndProductId(@Param("orderId") long orderId, @Param("productId") long productId);


    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.product.id = :productId")
    OrderItem findByOrderIdAndProductId(@Param("orderId") long orderId ,@Param("productId") long productId);
}