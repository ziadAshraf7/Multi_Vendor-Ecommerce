package com.example.ecommerce_app.Dto.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SuccessfulLoginInfo {

   private String jwtToken;

   private long userId;

}
