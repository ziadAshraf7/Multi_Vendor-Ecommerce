package com.example.ecommerce_app.Dto.Vendor_Product_Table;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Link_Vendor_Product_Dto extends Vendor_Product_Creation_Dto {


    @NotNull
    private long productId;




}
