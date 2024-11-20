package com.example.ecommerce_app.Dto.Product_Table;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.VendorProductOverviewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductOverviewDto {

    private long productId;

    private String name;

    private String title;

    private int rating;

    private String brandName;

    private String subCategoryName;

    private String thumbNail;

    private List<VendorProductOverviewDto> vendorProductsDto;

}
