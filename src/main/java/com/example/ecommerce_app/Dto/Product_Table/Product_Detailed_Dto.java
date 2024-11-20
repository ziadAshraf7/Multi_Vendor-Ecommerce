package com.example.ecommerce_app.Dto.Product_Table;

import com.example.ecommerce_app.Dto.Attribute_Table.AttributeDto;
import com.example.ecommerce_app.Dto.ProductAttributeValueTable.ProductAttributeValueDto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.VendorProductOverviewDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product_Detailed_Dto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String title;

    @Min(value = 0)
    private int RatingCount;

    @Max(value = 5)
    @Min(value = 0)
    private int rating;

    private String thumbNail;

    private String brandName;

    private String subCategoryName;

    private String description;

    private List<VendorProductOverviewDto> vendorProductOverviewDtos;

    private List<ProductReview_Detailed_Dto> reviewsDtos;

    private Map<AttributeDto , List<ProductAttributeValueDto>> attributeDtoListMap;

    private  List<String> imageFilesName;

}
