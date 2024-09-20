package com.example.ecommerce_app.Dto.Brand_Table;


import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class BrandResponseDto extends BrandCreationDto {

    public BrandResponseDto(){
        super();
    }

    @NotNull
    List<Product_Overview_Dto> product_overview_dtos;

}
