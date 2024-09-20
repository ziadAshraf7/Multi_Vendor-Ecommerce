package com.example.ecommerce_app.Repositery.Vendor_Product_Image;

import com.example.ecommerce_app.Entity.Vendor_Product_Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Vendor_Product_Image_Repository extends JpaRepository<Vendor_Product_Image , Long> {


    @Query("SELECT i FROM Vendor_Product_Image i WHERE i.vendor.id = :vendorId And i.product.id = :productId")
    List<Vendor_Product_Image> getAllImagesPerVendorProduct(@Param("vendorId") long vendorId , @Param("productId") long productId);

    @Query("DELETE FROM Vendor_Product_Image i WHERE i.vendor.id = :vendorId And i.product.id = :productId")
    @Modifying
    @Transactional
    void removeAllImagesPerVendorProduct(@Param("vendorId") long vendorId , @Param("productId") long productId);

}
