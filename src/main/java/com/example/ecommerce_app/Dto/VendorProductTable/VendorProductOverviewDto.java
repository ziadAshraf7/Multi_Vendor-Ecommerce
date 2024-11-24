package com.example.ecommerce_app.Dto.VendorProductTable;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorProductOverviewDto {

  private long vendorProductId;

  private String vendorName;

  private double discount;

  private double price;
}
