package com.example.ecommerce_app.Services.Product;

import com.example.ecommerce_app.Dto.Product_Table.Enums.ProductSortingByDtoEnum;
import com.example.ecommerce_app.Dto.Product_Table.Enums.SortingDirection;
import com.example.ecommerce_app.Dto.Product_Table.ProductFilterDto;
import com.example.ecommerce_app.Entity.Brand;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.VendorProduct;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductSpecification {

    private final BrandService brandService;

    private final CategoryService categoryService;

    public  Specification<Product> filterProducts(
            ProductFilterDto productFilterDto) {

        return (root, query, criteriaBuilder) -> {
            Join<Product, VendorProduct> vendorProductJoin = root.join("vendor_products", JoinType.LEFT);

            Predicate predicate = criteriaBuilder.conjunction();

            Double minPrice = null;
            Double maxPrice = null;

            if (productFilterDto.getPriceRange() != null && productFilterDto.getPriceRange().length == 2) {
                minPrice = productFilterDto.getPriceRange()[0];
                maxPrice = productFilterDto.getPriceRange()[1];
            }

            Long brandId = productFilterDto.getBrandId();
            Long categoryId = productFilterDto.getCategoryId();
            Integer rating = productFilterDto.getRating();
            Boolean isDiscount = productFilterDto.getIsDiscount();
            String sortBy = productFilterDto.getSortBy() == null ? null : productFilterDto.getSortBy().name().toLowerCase();
            SortingDirection sortingDirection = productFilterDto.getSortingDirection();

            if (rating != null) {
                if( rating > 5 || rating < 0.5) throw new CustomConflictException("rating Conflict error");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), rating));
            }

            if (categoryId != null) {
                Category subCategory = categoryService.getSubCategoryEntityById(categoryId);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("subCategory").get("id"), subCategory.getId()));
            }

            if (brandId != null) {
                Brand brand = brandService.getBrandEntityById(brandId);
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("brand").get("id"), brand.getId()));
            }

            if (minPrice != null & maxPrice != null) {
                if(minPrice > maxPrice) throw new CustomConflictException("Price Range Conflict error");

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.between(vendorProductJoin.get("price"), minPrice, maxPrice));
            }

            if (isDiscount != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThan(vendorProductJoin.get("discount"),0));
            }


            if(sortBy !=null && !sortBy.isEmpty()){
                Path<?> sortPath = null;
                if (sortBy.equals(ProductSortingByDtoEnum.price.name())) {
                    sortPath = vendorProductJoin.get(sortBy);
                } else if(sortBy.equals(ProductSortingByDtoEnum.rating.name())){
                    sortPath = root.get(sortBy);
                }

                Order order = (sortingDirection == SortingDirection.ASC)
                        ? criteriaBuilder.asc(sortPath)
                        : criteriaBuilder.desc(sortPath);
                query.orderBy(order);
            }


            return predicate;
        };
    }
}
