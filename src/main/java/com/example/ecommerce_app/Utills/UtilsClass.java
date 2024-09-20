package com.example.ecommerce_app.Utills;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
public class UtilsClass {

    static public double calcProductFinalPrice(double price , double discount){
        return (price - (price * (discount / 100)));
    }

}
