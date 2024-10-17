package com.example.ecommerce_app.Services;

import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Update_Dto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Mapper.ProductMapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Services.Brand.BrandServiceImp;
import com.example.ecommerce_app.Services.Category.CategoryServiceImp;
import com.example.ecommerce_app.Services.Product.ProductServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private  ProductMapper productMapper;

    @Mock
    private CategoryServiceImp categoryService;

    @InjectMocks
    private ProductServiceImp productService;

    @Mock
    private  PageRequest pageable = PageRequest.of(0, 10);

    @Mock
    private BrandServiceImp brandService;

    private Product product;

    @BeforeEach
    void init(){
        pageable = PageRequest.of(0, 10);

        product = Product
                .builder()
                .id(10L)
                .name("prod1")
                .title("title")
                .build();
    }



    @Test
    void ProductServiceUpdateProductReturnsProductOverviewDto(){

        Product_Update_Dto productUpdateDto = Product_Update_Dto.
                builder()
                .productId(10L)
                .title("newTitle")
                .build();

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> productService.getProductEntityById(10L));
        assertDoesNotThrow(() -> productService.updateProduct(productUpdateDto) );

        assertEquals("newTitle", product.getTitle());

        verify(productRepository).save(product);
    }


    @Test
    void ProductServiceUpdateProductRatingReturnsVoid(){
        int prevRatingsCount = product.getRatingCount();
        assertDoesNotThrow(() -> productService.updateProductRating(2 ,product ));

        assertEquals(product.getRatingCount() , prevRatingsCount + 1);
    }

    @Test
    void ProductServiceGetProductsByCategoryIdReturnsPagesOfProductOverviewDto(){

        long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("category1").build();
        Product product1 = Product.builder().id(2L).build();
        Product product2 = Product.builder().id(3L).build();
        List<Product> products = new ArrayList<>(List.of(product1, product2));

        when(categoryService.getCategoryEntityById(categoryId)).thenReturn(category);
        when(productRepository.findBySubCategory(category, pageable)).thenReturn(new PageImpl<>(products, pageable, products.size()));
        when(productMapper.to_Product_Overview_Dto(any(Product.class))).thenReturn(new Product_Overview_Dto());

        assertDoesNotThrow(() -> {
            Page<Product_Overview_Dto> result = productService.getProductsByCategoryId(categoryId);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
        });

        verify(productMapper, times(products.size())).to_Product_Overview_Dto(any());

    }



    @Test
    void ProductServiceGetProductByIdReturnsProductDetailedDto() {
        long productId = 30L;
        Product product = Product.builder().id(productId).build();
        Product_Detailed_Dto expectedDto = new Product_Detailed_Dto();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product) );
        when(productMapper.to_Product_Detailed_Dto(product)).thenReturn(expectedDto);

        assertDoesNotThrow(() -> productService.getProductById(productId));
    }


    @Test
    void ProductServiceGetProductByNameReturnsProductDetailedDto(){
        String productName = "product";
        Product product = Product.builder().name(productName).build();

        Product_Detailed_Dto productDto = new Product_Detailed_Dto();
        productDto.setName(productName);

        when(productRepository.findByName(productName)).thenReturn(product);
        when(productMapper.to_Product_Detailed_Dto(any(Product.class))).thenReturn(productDto);

        assertDoesNotThrow(() -> {
            Product_Detailed_Dto productDetailedDto = productService.getProductByName(productName);
            assertNotNull(productDetailedDto);
            assertEquals(productDetailedDto.getName() , product.getName());
        });

    }

    @Test
    void ProductServiceGetNewArrivalProductsReturnsProductDetailedDto(){

        long categoryId = 1L;

        Product product1 = Product.builder().build();
        Product product2 = Product.builder().build();
        List<Product> products = new ArrayList<>(List.of(product1, product2));

        when(productRepository.getFeaturedProducts(categoryId , pageable)).thenReturn(new PageImpl<>(products , pageable , products.size()));
        when(productMapper.to_Product_Overview_Dto(any(Product.class))).thenReturn(new Product_Overview_Dto());

        assertDoesNotThrow(() -> {
            Page<Product_Overview_Dto> result =  productService.getNewArrivalProducts(categoryId);

            assertNotNull(result);
            assertEquals(result.getTotalElements() , products.size());
            verify(productMapper , times(products.size())).to_Product_Overview_Dto(any(Product.class));
        });

    }


      @Test
      void ProductServiceGetDiscountProductsReturnsProductOverviewDto(){
          long categoryId = 1L;

          Product product1 = Product.builder().build();
          Product product2 = Product.builder().build();
          List<Product> products = new ArrayList<>(List.of(product1, product2));

          when(productRepository.getDiscountProducts(categoryId , pageable)).thenReturn(new PageImpl<>(products , pageable , products.size()));
          when(productMapper.to_Product_Overview_Dto(any(Product.class))).thenReturn(new Product_Overview_Dto());

          assertDoesNotThrow(() -> {
              Page<Product_Overview_Dto> result =  productService.getDiscountProducts(categoryId);

              assertNotNull(result);
              assertEquals(result.getTotalElements() , products.size());
              verify(productMapper , times(products.size())).to_Product_Overview_Dto(any(Product.class));
      });

    }


    @Test
    void ProductServiceGetProductEntityByIdReturnsProduct(){
        long productId = 1L;

        Product product = Product.builder().id(productId).build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> {

          Product result =  productService.getProductEntityById(productId);

          assertNotNull(result);
        });

    }


    @Test
    void ProductServiceGetProductsPerBrandReturnsPageOfProductOverviewDto(){
        long brandId = 1L;

        Product product1 = Product.builder().build();
        Product product2 = Product.builder().build();
        List<Product> products = new ArrayList<>(List.of(product1, product2));

        when(productRepository.findByBrandId(brandId , pageable)).thenReturn(new PageImpl<>(products , pageable , products.size()));
        when(productMapper.to_Product_Overview_Dto(any(Product.class))).thenReturn(new Product_Overview_Dto());

        assertDoesNotThrow(() -> {
            Page<Product_Overview_Dto> result =  productService.getProductsPerBrand(brandId);

            assertNotNull(result);
            assertEquals(result.getTotalElements() , products.size());
            verify(productMapper , times(products.size())).to_Product_Overview_Dto(any(Product.class));
        });
    }


    @Test
    void ProductServiceRemoveProductReturnsVoid(){
        long productId = 1L;
        Product product = Product.builder().id(productId).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> productService.removeProduct(productId));
        verify(productRepository, times(1)).deleteById(productId);

    }


    @Test
    @DisplayName("product not found exception")
    void ProductServiceUpdateProductThrowsCustomNotFoundExceptionWhenProductNotFound(){
        Product_Update_Dto productUpdateDto = Product_Update_Dto.builder().productId(1L).build();

        when(productRepository.findById(1L)).thenThrow(new CustomNotFoundException("Unable to find Product with Id " + 1L));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> {
             productService.updateProduct(productUpdateDto);
        });


        assertEquals(exception.getMessage() , "Unable to find Product with Id " + 1L);
        verify(productRepository , never()).save(any(Product.class));
    }

    @Test
    @DisplayName("product not found exception")
    void ProductServiceUpdateProductThrowsDatabasePersistenceException(){
        Product_Update_Dto productUpdateDto = Product_Update_Dto.builder().productId(10L).build();

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenThrow(new DatabasePersistenceException("Unable to update Product with id 10"));

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class , () -> {
            productService.updateProduct(productUpdateDto);
        });

        assertEquals(exception.getMessage() , "Unable to update Product with id 10");
        verify(productMapper , never()).to_Product_Overview_Dto(any(Product.class));
    }

    @Test
    @DisplayName("getting product by category id when category is not found exception")
    void ProductServiceGetProductsByCategoryIdThrowsDatabaseCustomNotFoundException(){

        when(categoryService.getCategoryEntityById(1L)).thenThrow(new CustomNotFoundException("Unable to find category with category id " + 1));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> productService.getProductsByCategoryId(1));

        assertEquals(exception.getMessage() , "Unable to find category with category id " + 1);
        verify(productRepository , never()).findBySubCategory(any(Category.class) , any(Pageable.class));

    }


    @Test
    @DisplayName("getting product by  id when product is not found exception")
    void ProductServiceGetProductsByIdThrowsDatabaseCustomNotFoundException(){

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class , () -> productService.getProductById(1));

        assertEquals(exception.getMessage() , "Unable to find Product with Id " + 1);
        verify(productMapper , never()).to_Product_Detailed_Dto(any());

    }

    @Test
    @DisplayName("getting products  per brand  when brand is not found exception")
    void ProductServiceGetProductsPerBrandThrowsDatabaseCustomNotFoundException(){

        when(brandService.getBrandEntityById(1L)).thenThrow( new CustomNotFoundException("cannot find brand with brand id " + 1));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class ,
                () -> productService.getProductsPerBrand(1));

        assertEquals(exception.getMessage() , "cannot find brand with brand id " + 1);
        verify(productRepository , never()).findByBrandId(anyLong() , any(Pageable.class));

    }

    @Test
    @DisplayName("removing product when product is not found exception")
    void ProductServiceRemoveProductThrowsDatabaseCustomNotFoundException(){

        when(productRepository.findById(1L)).thenThrow( new CustomNotFoundException("Unable to find Product with Id " + 1));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class ,
                () -> productService.removeProduct(1));

        assertEquals(exception.getMessage() , "Unable to find Product with Id " + 1);
        verify(productRepository , never()).deleteById(anyLong());

    }

    @Test
    @DisplayName("get product by name when product is not found exception")
    void ProductServiceGetProductByNameThrowsDatabaseCustomNotFoundException(){

        when(productRepository.findByName("product")).thenThrow( new CustomNotFoundException("Unable to find product name product"));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class ,
                () -> productService.getProductByName("product"));

        assertEquals(exception.getMessage() , "Unable to find product name product");
        verify(productMapper , never()).to_Product_Detailed_Dto(any());

    }

    @Test
    @DisplayName("get new arrival by category id when category is not found exception")
    void ProductServiceGetNewArrivalProductsThrowsDatabaseCustomNotFoundException(){

        when(categoryService.getCategoryEntityById(1L))
                .thenThrow( new CustomNotFoundException("Unable to find category with category id " + 1));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class ,
                () -> productService.getNewArrivalProducts(1L));

        assertEquals(exception.getMessage() , "Unable to find category with category id " + 1);
        verify(productRepository , never()).getFeaturedProducts(anyLong() , any());

    }


    @Test
    @DisplayName("get discount products by category id when category is not found exception")
    void ProductServiceGetDiscountProductsThrowsDatabaseCustomNotFoundException(){

        when(categoryService.getCategoryEntityById(1L))
                .thenThrow( new CustomNotFoundException("Unable to find category with category id " + 1));

        CustomNotFoundException exception =  assertThrows(CustomNotFoundException.class ,
                () -> productService.getDiscountProducts(1L));

        assertEquals(exception.getMessage() , "Unable to find category with category id " + 1);
        verify(productRepository , never()).getDiscountProducts(anyLong() , any());

    }
}
