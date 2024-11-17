package com.example.ecommerce_app.Dto.Product_Table;

import com.example.ecommerce_app.Dto.Product_Table.Enums.ProductSortingByDtoEnum;
import com.example.ecommerce_app.Dto.Product_Table.Enums.SortingDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductFilterDto {

   private Long brandId;

   private Long categoryId;

   private double[] priceRange;

   private Integer rating;

   private Boolean isDiscount;

   private ProductSortingByDtoEnum sortBy;

   private SortingDirection sortingDirection;
}
