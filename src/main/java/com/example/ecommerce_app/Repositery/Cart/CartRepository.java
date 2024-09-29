package com.example.ecommerce_app.Repositery.Cart;

import com.example.ecommerce_app.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart , Long> {

    @Query("SELECT c FROM Cart c WHERE c.customer.id = :customerId")
    Cart findByCustomerId(@Param("customerId") long customerId);

}
