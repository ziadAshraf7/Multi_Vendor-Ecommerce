package com.example.ecommerce_app.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
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


    @Lob
    @Column(name = "image" , columnDefinition = "LONGBLOB" , nullable = false)
    private byte[] image;


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

    @ManyToMany(cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JoinTable(
            name = "sub_category_attributes",
            joinColumns = { @JoinColumn(name = "sub_category_id") },
            inverseJoinColumns = { @JoinColumn(name = "sub_category_attribute_id") }
    )
    private final Set<Sub_Category_Attribute> subCategoryAttributes = new HashSet<>();


    public void addSubCategory(Category category){
        subCategories.add(category);
    }


}
