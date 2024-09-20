package com.example.ecommerce_app.Services.Brand;

import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandUpdateDto;
import com.example.ecommerce_app.Entity.Brand;
import com.example.ecommerce_app.Mapper.BrandMapper;
import com.example.ecommerce_app.Repositery.Brand.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class BrandServiceImp implements BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public void addBrand(BrandCreationDto brandCreationDto){
        Brand brand = brandMapper.fromCreationDtoToEntity(brandCreationDto);

        try {
            brandRepository.save(brand);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public BrandResponseDto getBrandById(long brandId) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("brand is not exists"));

        return brandMapper.fromEntityToOverviewDto(brand);

    }

    @Override
    public BrandResponseDto getBrandByName(String brandName) {
        try {
            Brand brand = brandRepository.findByName(brandName);
            return brandMapper.fromEntityToOverviewDto(brand);

        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteBrandById(long brandId) {
        try {
            brandRepository.deleteById(brandId);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBrand(BrandUpdateDto brandUpdateDto) throws IOException {
        try {
            Brand brand = brandRepository.findById(brandUpdateDto.getBrandId())
                    .orElseThrow(() -> new RuntimeException("brand is not found"));
            if(brandUpdateDto.getName() != null) brand.setName(brandUpdateDto.getName());
            if (brandUpdateDto.getImage() != null) brand.setImage(brandUpdateDto.getImage().getBytes());
            brandRepository.save(brand);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }

    }

}
