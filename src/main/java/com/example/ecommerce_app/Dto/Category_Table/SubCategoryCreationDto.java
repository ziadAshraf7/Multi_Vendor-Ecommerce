package com.example.ecommerce_app.Dto.Category_Table;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class SubCategoryCreationDto extends ParentCategoryCreationDto {

    @NotNull(message = "parent_Category Id cannot be null")
    private long parentCategoryId;

}
