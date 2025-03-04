package com.example.ecommerce_app.Repositery.Vendor_Product;


import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductBrandInfoDto;
import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductCategoryInfoDto;
import com.example.ecommerce_app.Entity.VendorProduct;
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
public interface VendorProductRepository extends JpaRepository<VendorProduct, Long> {

    @Query("Delete FROM VendorProduct vp WHERE vp.vendor.id = :vendorId And vp.product.id = :productId")
    @Modifying
    @Transactional
    void deleteVendorProduct(@Param("vendorId") long vendorId , @Param("productId") long productId);

    @Query("SELECT vp FROM VendorProduct vp WHERE vp.id = :vendorProductId")
    @EntityGraph(attributePaths = {"product"})
    VendorProduct findByVendorProductId(long vendorProductId);


    VendorProduct findByVendorIdAndProductId(long vendorId, long productId);

    Page<VendorProduct> findByVendorId(long vendorId , Pageable pageable);

    @Query("""
                    Select new com.example.ecommerce_app.Dto.VendorProductTable.VendorProductCategoryInfoDto(
                    vp.stock , vp.price , vp.discount , p.name , p.title , p.rating , c.name)
                    From VendorProduct vp left join vp.product p left join vp.product.subCategory c
                    WHERE vp.vendor.id = :vendorId And c.id = :categoryId
                    """)
    Page<VendorProductCategoryInfoDto> findByVendorProductIdAndCategoryId(@Param("vendorId") long vendorId ,
                                                                          @Param("categoryId") long categoryId,
                                                                          Pageable pageable);


    @Query("""
                    Select new com.example.ecommerce_app.Dto.VendorProductTable.VendorProductBrandInfoDto(
                    vp.stock , vp.price , vp.discount , p.name , p.title , p.rating , b.name)
                    From VendorProduct vp left join vp.product p left join vp.product.brand b
                    WHERE vp.vendor.id = :vendorId And b.id = :brandId
                    """)
    Page<VendorProductBrandInfoDto> findByVendorProductIdAndBrandId(@Param("vendorId") long vendorId ,
                                                                    @Param("brandId") long brandId,
                                                                    Pageable pageable);



    @Query("SELECT vp FROM VendorProduct vp WHERE vp.id IN :idList")
    @EntityGraph(attributePaths = {"product"})
    Page<VendorProduct> findAllVendorProductById(List<Long> idList , Pageable pageable);

    @Query("SELECT vp FROM VendorProduct vp WHERE vp.id IN :idList")
    @EntityGraph(attributePaths = {"product"})
    List<VendorProduct> findAllVendorProductById(List<Long> idList  );
}
