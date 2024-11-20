package com.example.ecommerce_app.Repositery.Vendor_Product_Image;

import com.example.ecommerce_app.Entity.vendorProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface vendorProductImageRepository extends JpaRepository<vendorProductImage, Long> {


    @Query("SELECT i FROM vendorProductImage i WHERE i.vendor.id = :vendorId And i.product.id = :productId")
    List<vendorProductImage> getAllImagesPerVendorProduct(@Param("vendorId") long vendorId , @Param("productId") long productId);

    @Query("DELETE FROM vendorProductImage i WHERE i.vendor.id = :vendorId And i.product.id = :productId")
    @Modifying
    @Transactional
    void removeAllImagesPerVendorProduct(@Param("vendorId") long vendorId , @Param("productId") long productId);

}
