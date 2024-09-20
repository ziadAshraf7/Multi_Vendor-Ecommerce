package com.example.ecommerce_app.Repositery.Product;


import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Entity.Product;
import jakarta.persistence.NamedEntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product ,Long > {

    Product findByName(String productName);


    Page<Product> findBySubCategory(Category subCategory , Pageable pageable);


    @Query("SELECT p FROM Product p LEFT JOIN p.subCategory c WHERE c.id = :categoryId  ORDER BY p.createdAt ASC")
    Page<Product> getFeaturedProducts(@Param("categoryId") long categoryId , Pageable pageable);


    @Query("SELECT p from Product p LEFT JOIN p.subCategory c LEFT JOIN p.vendor_products vp WHERE vp.discount > 0 AND p.subCategory.id = :categoryId")
    Page<Product> getDiscountProducts(@Param("categoryId") long categoryId , Pageable pageable);


}
