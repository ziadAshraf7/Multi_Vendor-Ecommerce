package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.Vendor_Product_Table.VendorProductOverviewDto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Entity.Embedded_Ids.VendorProductEmbeddedId;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.VendorProduct;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserServiceImp;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        imports = {VendorProductEmbeddedId.class , Product.class , User.class}

)
public interface Vendor_Product_Mapper {

      ProductService productService = null ;

      UserServiceImp USER_SERVICE_IMP = null;

      @Mapping(target = "product" , ignore = true)
      @Mapping(target = "vendor"  , ignore = true)
      VendorProduct toEntity(
              Vendor_Product_Creation_Dto vendor_product_creation_dto

      );

      @Mapping( target = "vendorName" , expression = ("java(vendor_product.getVendor().getUserName())"))
      @Mapping(target = "vendorProductId" , source = "id")
      VendorProductOverviewDto to_Vendor_Product_Overview_Dto(VendorProduct vendor_product);


}
