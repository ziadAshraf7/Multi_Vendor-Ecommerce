package com.example.ecommerce_app.Dto.ProductReviewTable;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductReview_Detailed_Dto {


      private String customerName;

      private String productName;

      private byte[] image;

      private String description;

      private double rate;

}



