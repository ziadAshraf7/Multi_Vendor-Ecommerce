package com.example.ecommerce_app.Dto.VendorProductTable;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkVendorProductDto extends VendorProductCreationDto {


    @NotNull
    private long productId;




}
