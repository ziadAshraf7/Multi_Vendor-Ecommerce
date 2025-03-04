package com.example.ecommerce_app.Services.Brand;


import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandUpdateDto;
import com.example.ecommerce_app.Entity.Brand;

import java.io.IOException;
import java.util.List;

public interface BrandService {

    void addBrand(BrandCreationDto brandCreationDto) throws IOException;

    BrandResponseDto getBrandById(long brandId);

    BrandResponseDto getBrandByName(String brandName);

    void deleteBrandById(long brandId);

    void updateBrand(BrandUpdateDto brandUpdateDto) throws IOException;

    Brand getBrandEntityById(long brandId);

    List<BrandResponseDto> getAllBrands();
}
