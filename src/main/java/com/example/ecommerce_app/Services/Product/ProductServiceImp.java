package com.example.ecommerce_app.Services.Product;


import com.example.ecommerce_app.Dto.Product_Table.*;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Mapper.ProductMapper;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImp implements ProductService
 {
  private final ProductRepository productRepository;

  private final ProductMapper productMapper;

  private final CategoryRepository categoryRepository;

  private final PageRequest pageable = PageRequest.of(0, 10);


    @Override
    public Product_Overview_Dto updateProduct(Product_Update_Dto product_update_dto) {
        long productId = product_update_dto.getProductId();

        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException(""));

            if((product_update_dto.getName() != null)) product.setName(product_update_dto.getName());
            if((product_update_dto.getTitle() != null)) product.setTitle(product_update_dto.getTitle());
            if(product_update_dto.getUserRate() != null) updateProductRating(product_update_dto.getUserRate() , product);

            productRepository.save(product);

            return productMapper.to_Product_Overview_Dto(product);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateProductRating(int user_rate , Product product) {
        int productRatingCount = product.getRatingCount();
        int newProductRatingCount = productRatingCount + 1;
        int newRating = (user_rate / newProductRatingCount);
        product.setRatingCount(newProductRatingCount);
        product.setRating(newRating);

        productRepository.save(product);

    }

    @Override
    public Page<Product_Overview_Dto> getProductsByCategoryId(long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException(""));

        Page<Product> products = productRepository.findBySubCategory(category , pageable);

        return products.map(productMapper::to_Product_Overview_Dto);
           }


    @Override
    public Product_Detailed_Dto getProductById(long productId) {
       try {
           Product product = productRepository.findById(productId)
                   .orElseThrow(() -> new RuntimeException(""));

           return productMapper.to_Product_Detailed_Dto(product);

       }catch (RuntimeException e){
           throw new RuntimeException("");
       }

    }

    @Override
    public Product_Detailed_Dto getProductByName(String name) {
        try {
            Product product = productRepository.findByName(name);

            return productMapper.to_Product_Detailed_Dto(product);
        }catch (RuntimeException e){
            throw new RuntimeException("");
        }
    }


    @Override
    public Page<Product_Overview_Dto> getNewArrivalProducts(long categoryId) {

        Page<Product> products = productRepository.getFeaturedProducts(categoryId , pageable);

        return products.map(productMapper::to_Product_Overview_Dto);
    }

    @Override
    public Page<Product_Overview_Dto> getDiscountProducts(long categoryId) {
        Page<Product> products = productRepository.getDiscountProducts(categoryId , pageable);
                return products.map(productMapper::to_Product_Overview_Dto);
         }

     @Override
     public Page<Product_Overview_Dto> getBestSellerProductsPerCategory(long categoryId) {
         return null;
     }

     @Override
     public Page<Product_Overview_Dto> filterProducts(ProductFilterDto productFilterDto) {
         return null;
     }

     @Override
    public void removeProduct(long productId) {
        productRepository.deleteById(productId);
    }



}
