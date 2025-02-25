package com.example.ecommerce_app.Dto.Category_Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ParentCategoryCreationDto {

    @NotEmpty(message = "name can't be empty")
    @NotNull(message = "name can't be null ")
    private String name;

    @NotNull(message = "image can't be null")
    private MultipartFile image;

    @NotEmpty(message = "description can't be empty")
    @NotNull(message = "description can't be null")
    private String description;


}
