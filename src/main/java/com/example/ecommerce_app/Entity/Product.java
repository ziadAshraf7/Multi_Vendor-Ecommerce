package com.example.ecommerce_app.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@SuperBuilder
@NamedEntityGraph(
        name = "product-without-brand-category",
        attributeNodes = {
                @NamedAttributeNode("brand"),
                @NamedAttributeNode("subCategory"),
        }
)
public class Product extends BaseEntity {

    public Product(){
        super();
    }

    @Column(name = "name" , unique = true , nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "title" , nullable = false)
    @NotEmpty
    private String title;


    @Column(name = "rating_count" , columnDefinition = "INT DEFAULT 0", nullable = false)
    @Min(value = 0)
    private int RatingCount;

    @Column(name = "rating" , nullable = false)
    @Max(value = 5)
    @Min(value = 0)
    private int rating;

    @Column(name = "thumbnail"  , nullable = false)
    private String thumbNail;

    @ManyToOne(cascade = {CascadeType.PERSIST , CascadeType.REMOVE} , fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "category_id")
    private Category subCategory;

    @Column(name = "description" , columnDefinition = "TEXT" , nullable = false)
    private String description;

    @OneToMany(mappedBy = "product" , cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JsonIgnore
    private List<VendorProduct> vendor_products;


    @OneToMany(mappedBy = "product" , cascade = {CascadeType.PERSIST , CascadeType.REMOVE} )
    private List<ProductReview> reviews;


    @OneToMany(mappedBy = "product" , cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JsonIgnore
    private  List<vendorProductImage> imageFilesName;


    @OneToMany(mappedBy = "product" ,  cascade = {CascadeType.PERSIST , CascadeType.REMOVE} )
    List<ProductAttributeValue> attributeValues;

    public void addImage(List<vendorProductImage> vendorProductImageList){
        imageFilesName.addAll(vendorProductImageList);
    }

    public String getBrandName(){
        return brand.getName();
    }

    public String getSubCategoryName(){
        return subCategory.getName();
    }

}
