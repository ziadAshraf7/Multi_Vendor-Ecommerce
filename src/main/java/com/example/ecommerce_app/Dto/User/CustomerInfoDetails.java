package com.example.ecommerce_app.Dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class CustomerInfoDetails extends UserInfoDetails{

    private long cartId;

    public CustomerInfoDetails(){
        super();
    }


}
