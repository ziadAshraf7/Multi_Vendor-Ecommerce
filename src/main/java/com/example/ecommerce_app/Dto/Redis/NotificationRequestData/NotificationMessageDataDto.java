package com.example.ecommerce_app.Dto.Redis.NotificationRequestData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationMessageDataDto {


    private Map<String , Object> data;


}
