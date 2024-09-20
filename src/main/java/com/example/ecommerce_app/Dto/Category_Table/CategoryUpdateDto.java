package com.example.ecommerce_app.Dto.Category_Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryUpdateDto {

    private long categoryId;

    private String name;

    private MultipartFile image;

    private String description;

}
