package com.example.ecommerce_app.Repositery.Vendor_Product;


import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import com.example.ecommerce_app.Entity.Vendor_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Vendor_Product_Repository extends JpaRepository<Vendor_Product, Vendor_Product_EmbeddedId> {

    @Query("Delete FROM Vendor_Product vp WHERE vp.vendor.id = :vendorId And vp.product.id = :productId")
    @Modifying
    @Transactional
    void delete_vendor_product(@Param("vendorId") long vendorId , @Param("productId") long productId);


}
