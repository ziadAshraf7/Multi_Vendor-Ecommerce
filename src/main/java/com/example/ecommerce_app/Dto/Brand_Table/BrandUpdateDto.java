package com.example.ecommerce_app.Dto.Brand_Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandUpdateDto   {

    private long brandId;

    private String name;

    private MultipartFile image;

}
