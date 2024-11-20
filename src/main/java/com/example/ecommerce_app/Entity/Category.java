package com.example.ecommerce_app.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.w3c.dom.Attr;

import java.util.*;

@EqualsAndHashCode(callSuper = true , onlyExplicitlyIncluded = true)
@Entity
@Table(name = "category")
@Data
@SuperBuilder
@AllArgsConstructor
public class Category extends BaseEntity {


    public Category(){
        super();
    }

    @Column(name = "name" , unique = true , nullable = false)
    @NotEmpty
    private String name;


    @Column(name = "image" , nullable = false)
    private String imageFileName;


    @Column(name = "description" , columnDefinition = "TEXT",  nullable = false )
    @NotEmpty
    private String description;


    @ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;


    @OneToMany(mappedBy = "parentCategory")
    @JsonIgnore
    private final List<Category> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "subCategory" , cascade = CascadeType.ALL)
    @JsonIgnore
    private final List<Product> products = new ArrayList<>();

    @ManyToMany(mappedBy = "subCategories", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Attribute> subCategoryAttributes;


    public void addSubCategory(Category category){
        subCategories.add(category);
    }

    public void addAttribute(Attribute attribute){
        subCategoryAttributes.add(attribute);
    }

    public void removeAttribute(Attribute attribute){
        subCategoryAttributes.remove(attribute);
    }
}
