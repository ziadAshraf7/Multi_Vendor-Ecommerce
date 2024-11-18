package com.example.ecommerce_app.Dto.Brand_Table;


import com.example.ecommerce_app.Dto.Product_Table.ProductOverviewDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponseDto {

    List<ProductOverviewDto> product_overview_dtos;

    @NotEmpty(message = "brand name can't be empty")
    private String name;

    @NotNull(message = "image can't be null")
    private byte[] image;

}
