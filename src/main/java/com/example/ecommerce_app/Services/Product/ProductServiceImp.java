package com.example.ecommerce_app.Services.Product;


import com.example.ecommerce_app.Dto.Product_Table.*;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.*;
import com.example.ecommerce_app.Mapper.ProductMapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService
 {
  private final ProductRepository productRepository;

  private final ProductMapper productMapper;

  private CategoryService categoryService;

  private final ProductSpecification productSpecification;

  private BrandService brandService;

  private final FileSystemStorageService fileSystemStorageService;

    @Override
    @Transactional
    public ProductOverviewDto updateProduct(ProductUpdateDto productUpdateDto) {
        long productId = productUpdateDto.getProductId();

        try {
            Product product = getProductEntityById(productId);

            if((productUpdateDto.getName() != null)) product.setName(productUpdateDto.getName());
            if((productUpdateDto.getTitle() != null)) product.setTitle(productUpdateDto.getTitle());
            if(productUpdateDto.getUserRate() != null) updateProductRating(productUpdateDto.getUserRate() , product);
            if(productUpdateDto.getThumbNail() != null){
                fileSystemStorageService.deleteImageFile(product.getThumbNail());
                product.setThumbNail(fileSystemStorageService.saveImageToFileSystem(
                        productUpdateDto.getThumbNail()
                ));
            }

            productRepository.save(product);

            return productMapper.toProductOverviewDto(product);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to update Product with id " + productUpdateDto.getProductId());
        } catch (IOException e) {
            throw new CustomRuntimeException(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void updateProductRating(int user_rate , Product product) {
        try {
            if(user_rate > 5) throw new CustomBadRequestException("rate cannot be greater than 5");
            int productRatingCount = product.getRatingCount();
            int newProductRatingCount = productRatingCount + 1;
            int newRating = (user_rate / newProductRatingCount);
            product.setRatingCount(newProductRatingCount);
            product.setRating(newRating);

            productRepository.save(product);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to Update product name  " + product.getName() );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductOverviewDto> getProductsByCategoryId(long categoryId , Pageable pageable) {

        Page<Product> products = productRepository.findBySubCategoryId(categoryId , pageable);

        return products.map(productMapper::toProductOverviewDto);
     }



    @Override
    @Transactional(readOnly = true)
    public Page<ProductOverviewDto> getNewArrivalProducts(long categoryId , Pageable pageable) {
            categoryService.getCategoryEntityById(categoryId);
            Page<Product> products = productRepository.getFeaturedProducts(categoryId , pageable);
            return products.map(productMapper::toProductOverviewDto);
     }


    @Override
    @Transactional(readOnly = true)
    public Page<ProductOverviewDto> getDiscountProducts(long categoryId , Pageable pageable) {
        categoryService.getCategoryEntityById(categoryId);
        Page<Product> products = productRepository.getDiscountProducts(categoryId , pageable);
        return products.map(productMapper::toProductOverviewDto);
      }

     @Override
     @Transactional(readOnly = true)
     public Page<ProductOverviewDto> getBestSellerProductsPerBrand(long brandId , Pageable pageable) {
         pageable = PageRequest.of(
                 pageable.getPageNumber(),
                 pageable.getPageSize(),
                 Sort.by(Sort.Direction.DESC, "salesCount")
         );

        Page<Product> products = productRepository.findByBrandId(brandId , pageable);

        return products.map(productMapper::toProductOverviewDto);

     }


     @Override
     @Transactional(readOnly = true)
     public Page<ProductOverviewDto> getBestSellerProductsPerCategory(long categoryId , Pageable pageable) {
         pageable = PageRequest.of(
                 pageable.getPageNumber(),
                 pageable.getPageSize(),
                 Sort.by(Sort.Direction.DESC, "salesCount")
         );

         Page<Product> products = productRepository.findBySubCategoryId(categoryId , pageable);

         return products.map(productMapper::toProductOverviewDto);
     }

     @Override
     public Page<ProductOverviewDto> getMostViewedProductsPerBrand(long brandId, Pageable pageable) {
         pageable = PageRequest.of(
                 pageable.getPageNumber(),
                 pageable.getPageSize(),
                 Sort.by(Sort.Direction.DESC, "viewsCount")
         );

         Page<Product> products = productRepository.findByBrandId(brandId , pageable);

         return products.map(productMapper::toProductOverviewDto);
     }

     @Override
     public Page<ProductOverviewDto> getMostViewedProductsPerCategory(long categoryId, Pageable pageable) {
         pageable = PageRequest.of(
                 pageable.getPageNumber(),
                 pageable.getPageSize(),
                 Sort.by(Sort.Direction.DESC, "viewsCount")
         );

         Page<Product> products = productRepository.findBySubCategoryId(categoryId , pageable);

         return products.map(productMapper::toProductOverviewDto);
     }

     @Override
     @Transactional(readOnly = true)
     public Page<ProductOverviewDto> filterProducts(ProductFilterDto productFilterDto , Pageable pageable) {
         Specification<Product> predicate = productSpecification.filterProducts(productFilterDto);
         Page<Product> productPage = productRepository.findAll(predicate, pageable);
         return productPage.map(productMapper::toProductOverviewDto);
    }

     @Override
     @Transactional(readOnly = true)
     public Product getProductEntityById(long productId) {
         return productRepository.findById(productId)
                 .orElseThrow(() -> new CustomNotFoundException("Unable to find Product with Id " + productId));
     }

     @Override
     public Page<ProductOverviewDto> getProductsPerBrand(long brandId , Pageable pageable) {

            brandService.getBrandEntityById(brandId);

            Page<Product> products = productRepository.findByBrandId(brandId , pageable);

            return products.map(productMapper::toProductOverviewDto);

          }

     @Override
     @Transactional
     public void removeProduct(long productId) {
        try {
            Product product = getProductEntityById(productId);
//            fileSystemStorageService.deleteImageFile(product.getThumbNail());
            productRepository.deleteById(product.getId());
        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Unable to delete product id " + productId);
        }
    }



}
