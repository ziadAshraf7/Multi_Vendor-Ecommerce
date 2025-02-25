package com.example.ecommerce_app.Services;

import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.ProductAttributeValue.ProductAttributeValueRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product_Image.vendorProductImageRepository;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import com.example.ecommerce_app.Services.ProductManagement.ProductManagementServiceImp;
import com.example.ecommerce_app.Services.User.UserServiceImp;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductManagementServiceTest {

    @Mock
    private  BrandService brandService;

    @Mock
    private  ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private VendorProductRepository vendor_product_repository;

    @Mock
    private  UserServiceImp userServiceImp;

    @Mock
    private ProductAttributeValueRepository productAttributeValueRepository;

    @Mock
    private vendorProductImageRepository vendor_product_image_repository;

    @InjectMocks
    private ProductManagementServiceImp productManagementService;

    private Brand brand;

    private Category subCategory ;

    private User vendor;


    private MultipartFile mockThumbNail;

    private ProductCreationDto productCreationDto;

    private Product product;

    @BeforeEach
    void init() {

         Category parentCategory = Category.builder().parentCategory(null).name("parentCategory").build();
         brand = Brand.builder().id(1L).name("brand").build();
         subCategory = Category.builder().parentCategory(parentCategory).id(1L).name("category").build();
         vendor = User.builder().id(1L).userName("vendor").userRole(UserRoles.ROLE_VENDOR).build();


        mockThumbNail = mock(MultipartFile.class);

        productCreationDto = ProductCreationDto
                .builder()
                .brandId(1L)
                .thumbNail( mockThumbNail)
                .subCategoryId(1L)
                .vendorId(1L)
                .productAttributesWithValues(Map.of())
                .imageFiles(List.of(mockThumbNail))
                .build();

        product = Product.builder().id(1L)
                .brand(brand)
                .subCategory(subCategory).build();

    }

    @Test
    @DisplayName("Persisting Product Entity with Linking with a Vendor and Adding Product Images and Attributes attached values ")
    void ProductManagementServiceAddProductReturnsVoid() throws IOException {

        VendorProduct vendor_product = VendorProduct.builder().product(product).vendor(vendor).stock(5).price(50.0).build();

        List<vendorProductImage> vendorProductImages = new ArrayList<>(List.of(vendorProductImage.builder()
                .vendor(vendor)
                .id(1L)
                .product(product)
                .image(mockThumbNail.getBytes())
                .build()));

        List<ProductAttributeValue> attributeValues = new ArrayList<>(List.of(
                ProductAttributeValue.builder().product(product)
                        .attribute(Attribute.builder().build())
                        .build()));


        when(mockThumbNail.getBytes()).thenReturn(new byte[]{});
        when(brandService.getBrandEntityById(1L)).thenReturn(brand);
        when(categoryService.getSubCategoryEntityById(1L)).thenReturn(subCategory);
        when(productAttributeValueRepository.saveAll(anyList())).thenReturn(attributeValues);
        when(userServiceImp.getUserEntityById(1L , UserRoles.ROLE_VENDOR)).thenReturn(vendor);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(vendor_product_repository.save(any(VendorProduct.class))).thenReturn(vendor_product);
        when(vendor_product_image_repository.saveAll(anyList())).thenReturn(vendorProductImages);


        assertDoesNotThrow(() -> {
            productManagementService.addProduct(productCreationDto);
        });


        verify(brandService).getBrandEntityById(1L);
        verify(categoryService).getSubCategoryEntityById(1L);
        verify(userServiceImp).getUserEntityById(1L, UserRoles.ROLE_VENDOR);
        verify(productAttributeValueRepository).saveAll(anyList());
        verify(productRepository).save(any(Product.class));
        verify(vendor_product_repository).save(any(VendorProduct.class) );
        verify(vendor_product_image_repository).saveAll(anyList());
    }

    @Test
    @DisplayName("brand not found exception")
    void ProductManagementServiceAddProductTrowsCustomNotFoundExceptionWhenBrandNotFound(){

        when(brandService.getBrandEntityById(1L)).thenThrow(new CustomNotFoundException("brand is not found"));

        assertThrows(CustomNotFoundException.class , () -> productManagementService.addProduct(productCreationDto));

        verify(productRepository , never()).save(any(Product.class));

    }

    @Test
    @DisplayName("category not found exception")
    void ProductManagementServiceAddProductTrowsCustomNotFoundExceptionWhenCategoryNotFound(){

        when(categoryService.getSubCategoryEntityById(1L)).thenThrow(new CustomNotFoundException("Unable to find category with category id " + 1L));

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class , () -> productManagementService.addProduct(productCreationDto));

        verify(productRepository , never()).save(any(Product.class));
        assertEquals(exception.getMessage() , "Unable to find category with category id " + 1L);
    }


    @Test
    @DisplayName("category is not a subCategory exception")
    void ProductManagementServiceAddProductTrowsCustomConflictExceptionWhenXCategoryIsNotSubCategory(){
        Category category = Category.builder().parentCategory(null).id(1L).build();

        when(categoryService.getSubCategoryEntityById(1L)).thenReturn(category);
        when(categoryService.getSubCategoryEntityById(1L))
                .thenThrow(new CustomConflictException("category with id 1 is not a sub_category"));

        CustomConflictException exception = assertThrows(CustomConflictException.class ,
                () -> productManagementService.addProduct(productCreationDto));

        verify(productRepository , never()).save(any(Product.class));
        assertEquals(exception.getMessage() , "category with id 1 is not a sub_category");
    }

    @Test
    @DisplayName("user not found exception")
    void ProductManagementServiceAddProductTrowsCustomNotFoundExceptionWhenUserIsNotFound(){

        when(userServiceImp.getUserEntityById(1L , UserRoles.ROLE_VENDOR)).thenThrow(new CustomNotFoundException("user is not found for user id " + 1L));

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class ,
                () -> productManagementService.addProduct(productCreationDto));

        verify(productRepository , never()).save(any(Product.class));
        assertEquals(exception.getMessage() , "user is not found for user id " + 1L);
    }

    @Test
    @DisplayName("user is not a vendor exception")
    void ProductManagementServiceAddProductTrowsCustomConflictExceptionWhenUserIsNotVendor(){

        User user = User.builder().userRole(UserRoles.ROLE_CUSTOMER).build();

        when(userServiceImp.getUserEntityById(1L , UserRoles.ROLE_VENDOR)).thenReturn(user);
        when(userServiceImp.getUserEntityById(1L , UserRoles.ROLE_VENDOR)).thenThrow(new CustomConflictException("User is not " + UserRoles.ROLE_VENDOR));

        CustomConflictException exception = assertThrows(CustomConflictException.class ,
                () -> productManagementService.addProduct(productCreationDto));

        verify(productRepository , never()).save(any(Product.class));
        assertEquals(exception.getMessage() , "User is not " + UserRoles.ROLE_VENDOR);
    }

    @Test
    @DisplayName("persisting product in the database exceptions")
    void ProductManagementServiceAddProductThrowsDatabasePersistenceExceptionWhenPersistProduct(){


        when(productRepository.save(any(Product.class))).thenThrow(new DatabasePersistenceException("Unable to persist Product in Database"));

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class
                , () -> productManagementService.addProduct(productCreationDto));

        assertEquals(exception.getMessage() , "Unable to persist Product in Database");

        verify(brandService , times(1)).getBrandEntityById(1L);
        verify(categoryService , times(1)).getSubCategoryEntityById(1L);
        verify(userServiceImp , times(1)).getUserEntityById(1L , UserRoles.ROLE_VENDOR);


    }

    @Test
    @DisplayName("persisting vendor_product entity in the database exceptions")
    void ProductManagementServiceAddProductThrowsDatabasePersistenceExceptionWhenPersistVendorProduct()   {

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(vendor_product_repository.save(any(VendorProduct.class))).thenThrow(new DatabasePersistenceException("Unable to link the product with the vendor"));

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class
                , () -> productManagementService.addProduct(productCreationDto));

        assertEquals(exception.getMessage() , "Unable to link the product with the vendor");

        verify(productRepository , times(1)).save(any(Product.class));
        verify(brandService , times(1)).getBrandEntityById(1L);
        verify(categoryService , times(1)).getSubCategoryEntityById(1L);
        verify(userServiceImp , times(1)).getUserEntityById(1L , UserRoles.ROLE_VENDOR);


    }


    @Test
    @DisplayName("persisting vendor_product_image entity in the database exceptions")
    void ProductManagementServiceAddProductThrowsDatabasePersistenceExceptionWhenPersistVendorProductImage()   {

          VendorProduct vendorProduct = VendorProduct
                .builder()
                .vendor(vendor)
                .product(product)
                .stock(50)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(vendor_product_repository.save(any(VendorProduct.class))).thenReturn(vendorProduct);
        when(vendor_product_image_repository.saveAll(anyList())).thenThrow(new DatabasePersistenceException("Unable to add Images for the Product"));

        DatabasePersistenceException exception =  assertThrows(DatabasePersistenceException.class
                , () -> productManagementService.addProduct(productCreationDto));

        assertEquals(exception.getMessage() , "Unable to add Images for the Product");

        verify(productRepository , times(1)).save(any(Product.class));
        verify(vendor_product_repository).save(any(VendorProduct.class));
        verify(brandService , times(1)).getBrandEntityById(1L);
        verify(categoryService , times(1)).getSubCategoryEntityById(1L);
        verify(userServiceImp , times(1)).getUserEntityById(1L , UserRoles.ROLE_VENDOR);


    }



}
