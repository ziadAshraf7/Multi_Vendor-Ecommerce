package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.Vendor_Product;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserServiceImp;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        imports = {Vendor_Product_EmbeddedId.class , Product.class , User.class}

)
public interface Vendor_Product_Mapper {

      ProductService productService = null ;

      UserServiceImp USER_SERVICE_IMP = null;

      @Mapping(target = "product" , ignore = true)
      @Mapping(target = "vendor"  , ignore = true)
      @Mapping(target = "id", expression = "java(new Vendor_Product_EmbeddedId())")
      Vendor_Product toEntity(
              Vendor_Product_Creation_Dto vendor_product_creation_dto

      );

      @Mapping( target = "vendorName" , expression = ("java(vendor_product.getVendor().getUserName())"))
      Vendor_Product_Overview_Dto to_Vendor_Product_Overview_Dto(Vendor_Product vendor_product);


}
