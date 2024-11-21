package com.example.ecommerce_app.Services.Brand;

import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandUpdateDto;
import com.example.ecommerce_app.Entity.Brand;
import com.example.ecommerce_app.Exceptions.Exceptions.*;
import com.example.ecommerce_app.Mapper.BrandMapper;
import com.example.ecommerce_app.Repositery.Brand.BrandRepository;
import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BrandServiceImp implements BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    private final FileSystemStorageService fileSystemStorageService;

    @Transactional
    public void addBrand(BrandCreationDto brandCreationDto) throws IOException {
        Brand brand = brandRepository.findByName(brandCreationDto.getName());

        if(brand != null) throw new CustomConflictException("brand name " + brandCreationDto.getName() + " is already exists");

        Brand newBrand = brandMapper.fromCreationDtoToEntity(brandCreationDto);
        newBrand.setImageFileName(fileSystemStorageService.saveImageToFileSystem(
                brandCreationDto.getImage()
        ));
        try {
            brandRepository.save(newBrand);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Brand with name " + brandCreationDto.getName() + " is already exists");
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
            Brand brand = brandRepository.findByName(brandName);
            if(brand == null) throw new CustomNotFoundException("Unable to find brand by brand name " + brandName);
            return brandMapper.fromEntityToOverviewDto(brand);
     }

    @Override
    @Transactional
    public void deleteBrandById(long brandId) {
        try {
            Brand brand = getBrandEntityById(brandId);
            fileSystemStorageService.deleteImageFile(brand.getImageFileName());
            brandRepository.deleteById(brand.getId());
        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("unable to delete the brand with id " + brandId);
        }
    }

    @Override
    @Transactional
    public void updateBrand(BrandUpdateDto brandUpdateDto) throws IOException {
            Brand brand = getBrandEntityById(brandUpdateDto.getBrandId());
            if(brandUpdateDto.getName() != null) brand.setName(brandUpdateDto.getName());
            if (brandUpdateDto.getImage() != null) {
                fileSystemStorageService.deleteImageFile(brand.getImageFileName());
                brand.setImageFileName(fileSystemStorageService.saveImageToFileSystem(brandUpdateDto.getImage()));
            };
        try {
            brandRepository.save(brand);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to Update the Brand with id " + brandUpdateDto.getBrandId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Brand getBrandEntityById(long brandId) {
         return  brandRepository.findById(brandId).orElseThrow(
                    () -> new CustomNotFoundException("cannot find brand with brand id " + brandId)
            );
    }

    @Transactional(readOnly = true)
    @Override
    public List<BrandResponseDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();

        List<BrandResponseDto> brandResponseDtoList = new ArrayList<>(brands.size());

        brands.forEach(brand -> brandResponseDtoList.add(
                BrandResponseDto
                        .builder()
                        .name(brand.getName())
                        .image(brand.getImageFileName())
                        .build()
        ));

        return brandResponseDtoList;
    }

}
