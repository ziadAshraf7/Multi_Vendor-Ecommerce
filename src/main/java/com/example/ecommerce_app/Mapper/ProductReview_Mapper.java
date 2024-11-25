package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReviewCreationDto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Update_Dto;
import com.example.ecommerce_app.Entity.Embedded_Ids.Product_Review_EmbeddedId;
import com.example.ecommerce_app.Entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring" ,
        imports = {Product_Review_EmbeddedId.class}
)
public interface ProductReview_Mapper {

    @Mapping(target = "imageFileName"  , expression = "java(productReview_creation_dto.getImage() != null ? productReview_creation_dto.getImage().getOriginalFilename() : null)")
    @Mapping(target = "user" , ignore = true)
    @Mapping(target = "product" , ignore = true)
    @Mapping(target = "id" , expression = "java(new Product_Review_EmbeddedId())")
    ProductReview fromCreationDtoToEntity(ProductReviewCreationDto productReview_creation_dto);

    @Mapping(target = "imageFileName"  , expression = "java(productReview_update_dto.getImage().getOriginalFilename())")
    @Mapping(target = "user" , ignore = true)
    @Mapping(target = "product" , ignore = true)
    @Mapping(target = "id" , expression = "java(new Product_Review_EmbeddedId())")
    ProductReview fromUpdateDtoEntity(ProductReview_Update_Dto productReview_update_dto);


    @Mapping(target = "customerName"  , expression = "java(productReview.getUser().getUserName())")
    @Mapping(target = "productName" , expression = ("java(productReview.getProduct().getName())"))
    ProductReview_Detailed_Dto to_ProductReview_Detailed_Dto(ProductReview productReview);


}
