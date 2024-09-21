package com.example.ecommerce_app.Dto.Category_Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class Sub_Category_Creation_Dto extends Parent_Category_Creation_Dto {

    @NotNull(message = "parent_Category Id cannot be null")
    private long parentCategoryId;

}
