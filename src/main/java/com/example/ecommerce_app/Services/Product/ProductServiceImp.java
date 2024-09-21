package com.example.ecommerce_app.Services.Product;


import com.example.ecommerce_app.Dto.Product_Table.*;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Mapper.ProductMapper;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Services.Category.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService
 {
  private final ProductRepository productRepository;

  private final ProductMapper productMapper;

  private CategoryService categoryService;

  private final PageRequest pageable = PageRequest.of(0, 10);


    @Override
    @Transactional
    public Product_Overview_Dto updateProduct(Product_Update_Dto product_update_dto) {
        long productId = product_update_dto.getProductId();

        try {
            Product product = getProductEntityById(productId);

            if((product_update_dto.getName() != null)) product.setName(product_update_dto.getName());
            if((product_update_dto.getTitle() != null)) product.setTitle(product_update_dto.getTitle());
            if(product_update_dto.getUserRate() != null) updateProductRating(product_update_dto.getUserRate() , product);

            productRepository.save(product);

            return productMapper.to_Product_Overview_Dto(product);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to update Product with id " + product_update_dto.getProductId());
        }

    }

    @Override
    @Transactional
    public void updateProductRating(int user_rate , Product product) {
        try {
            int productRatingCount = product.getRatingCount();
            int newProductRatingCount = productRatingCount + 1;
            int newRating = (user_rate / newProductRatingCount);
            product.setRatingCount(newProductRatingCount);
            product.setRating(newRating);

            productRepository.save(product);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to Update product name  " + product.getName() );
        }


    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product_Overview_Dto> getProductsByCategoryId(long categoryId) {

        Category category = categoryService.getCategoryEntityById(categoryId);

        Page<Product> products = productRepository.findBySubCategory(category , pageable);

        return products.map(productMapper::to_Product_Overview_Dto);
     }


    @Override
    @Transactional(readOnly = true)
    public Product_Detailed_Dto getProductById(long productId) {
           Product product = getProductEntityById(productId);
           return productMapper.to_Product_Detailed_Dto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product_Detailed_Dto getProductByName(String name) {
        try {
            Product product = productRepository.findByName(name);
            return productMapper.to_Product_Detailed_Dto(product);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new NotFoundException("Unable to find Product name " + name);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Product_Overview_Dto> getNewArrivalProducts(long categoryId) {
        try {
            Page<Product> products = productRepository.getFeaturedProducts(categoryId , pageable);
            return products.map(productMapper::to_Product_Overview_Dto);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to retrieve the Products");
        }
     }


    @Override
    @Transactional(readOnly = true)
    public Page<Product_Overview_Dto> getDiscountProducts(long categoryId) {
        try {
            Page<Product> products = productRepository.getDiscountProducts(categoryId , pageable);
            return products.map(productMapper::to_Product_Overview_Dto);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to retrieve the Products");
        }
      }

     @Override
     @Transactional(readOnly = true)
     public Page<Product_Overview_Dto> getBestSellerProductsPerCategory(long categoryId) {
         return null;
     }

     @Override
     @Transactional(readOnly = true)

     public Page<Product_Overview_Dto> filterProducts(ProductFilterDto productFilterDto) {
         return null;
     }

     @Override
     @Transactional(readOnly = true)
     public Product getProductEntityById(long productId) {
         return productRepository.findById(productId)
                 .orElseThrow(() -> new NotFoundException("Unable to find Product with Id " + productId));
     }

     @Override
     @Transactional
     public void removeProduct(long productId) {
        try {
            productRepository.deleteById(productId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to delete product id " + productId);
        }
    }



}
