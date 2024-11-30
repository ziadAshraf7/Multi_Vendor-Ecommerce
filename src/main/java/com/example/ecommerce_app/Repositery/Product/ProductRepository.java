package com.example.ecommerce_app.Repositery.Product;


import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product ,Long > , JpaSpecificationExecutor<Product> {

    Product findByName(String productName);

    @EntityGraph(attributePaths = {"vendor_products" , "brand" , "subCategory"})
    Page<Product> findBySubCategoryId(long subCategoryId , Pageable pageable);


    @Query("SELECT p FROM Product p LEFT JOIN p.subCategory c WHERE c.id = :categoryId  ORDER BY p.createdAt ASC")
    Page<Product> getFeaturedProducts(@Param("categoryId") long categoryId , Pageable pageable);


    @Query("SELECT p from Product p LEFT JOIN p.subCategory c LEFT JOIN p.vendor_products vp WHERE vp.discount > 0 AND p.subCategory.id = :categoryId")
    Page<Product> getDiscountProducts(@Param("categoryId") long categoryId , Pageable pageable);

    @EntityGraph(attributePaths = {"vendor_products" , "brand" , "subCategory"})
    Page<Product> findByBrandId(long brandId , Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"vendor_products" , "vendor_products.vendor", "brand" , "subCategory"})
    Page<Product> findAll(Specification<Product> productSpecification, Pageable pageable);

    @Query("SELECT p from Product p LEFT JOIN p.subCategory c LEFT JOIN p.vendor_products  LEFT JOIN p.brand  WHERE p.id = :productId")
    @EntityGraph(attributePaths = {"vendor_products" , "vendor_products.vendor", "brand" , "subCategory"})
    Product getEagerProductEntity(long productId);

    boolean existsByName(String name);

}
