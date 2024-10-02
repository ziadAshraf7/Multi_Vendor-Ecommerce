package com.example.ecommerce_app.Services.Brand;

import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandUpdateDto;
import com.example.ecommerce_app.Entity.Brand;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Mapper.BrandMapper;
import com.example.ecommerce_app.Repositery.Brand.BrandRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class BrandServiceImp implements BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    @Transactional
    public void addBrand(BrandCreationDto brandCreationDto){
        Brand brand = brandMapper.fromCreationDtoToEntity(brandCreationDto);

        try {
            brandRepository.save(brand);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add brand with name " + brandCreationDto.getName());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponseDto getBrandById(long brandId) {

        Brand brand = getBrandEntityById(brandId);

        return brandMapper.fromEntityToOverviewDto(brand);

    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponseDto getBrandByName(String brandName) {
        try {
            Brand brand = brandRepository.findByName(brandName);
            return brandMapper.fromEntityToOverviewDto(brand);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new NotFoundException("brand with name " + brandName + " is not found" );
        }
    }

    @Override
    @Transactional
    public void deleteBrandById(long brandId) {
        try {
            brandRepository.deleteById(brandId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("unable to delete the brand with id " + brandId);
        }
    }

    @Override
    @Transactional
    public void updateBrand(BrandUpdateDto brandUpdateDto) throws IOException {
        try {
            Brand brand = getBrandEntityById(brandUpdateDto.getBrandId());
            if(brandUpdateDto.getName() != null) brand.setName(brandUpdateDto.getName());
            if (brandUpdateDto.getImage() != null) brand.setImage(brandUpdateDto.getImage().getBytes());
            brandRepository.save(brand);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to Update the Brand with id " + brandUpdateDto.getBrandId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Brand getBrandEntityById(long brandId) {
        try {
            return brandRepository.getReferenceById(brandId);
        }catch (RuntimeException e){
            throw new NotFoundException("Unable to Find the Brand id " + brandId);
        }
    }

}
