package com.example.ecommerce_app.Dto.Paginating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class PageDto {
    public PageDto() {

    }

    private int pageNumber;
    private int pageSize;
}
