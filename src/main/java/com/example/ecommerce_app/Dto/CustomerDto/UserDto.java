package com.example.ecommerce_app.Dto.CustomerDto;

import com.example.ecommerce_app.Dto.Paginating.PageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserDto extends PageDto {
    public UserDto(){
        super();
    }

    private long customerId;
}
