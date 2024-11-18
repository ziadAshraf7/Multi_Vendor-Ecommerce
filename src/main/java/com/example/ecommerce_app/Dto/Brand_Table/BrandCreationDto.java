package com.example.ecommerce_app.Dto.Brand_Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BrandCreationDto {

    @NotEmpty(message = "brand name can't be empty")
    private String name;

    @NotNull(message = "image can't be null")
    private MultipartFile image;

}
