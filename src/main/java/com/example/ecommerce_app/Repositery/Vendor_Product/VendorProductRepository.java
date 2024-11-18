package com.example.ecommerce_app.Repositery.Vendor_Product;


import com.example.ecommerce_app.Entity.VendorProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VendorProductRepository extends JpaRepository<VendorProduct, Long> {

    @Query("Delete FROM VendorProduct vp WHERE vp.vendor.id = :vendorId And vp.product.id = :productId")
    @Modifying
    @Transactional
    void delete_vendor_product(@Param("vendorId") long vendorId , @Param("productId") long productId);

    @Query("SELECT vp FROM VendorProduct vp WHERE vp.id = :vendorProductId")
    VendorProduct findByVendorIdAndProductId(long vendorProductId);

    @Query("SELECT vp FROM VendorProduct vp WHERE vp.id IN :idList")
    @EntityGraph(attributePaths = {"product"})
    List<VendorProduct> findAllVendorProductById(List<Long> idList);
}
