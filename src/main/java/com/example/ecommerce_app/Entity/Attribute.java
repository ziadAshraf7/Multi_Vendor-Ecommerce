package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "attribute")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true , nullable = false)
    @NotEmpty
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JoinTable(
            name = "sub_category_attributes",
            joinColumns = { @JoinColumn(name = "attribute_id") },
            inverseJoinColumns = { @JoinColumn(name = "sub_category_id") }
    )
    private  Set<Category> subCategories;

    public void addNewSubCategory(Category category){
        subCategories.add(category);
        category.addAttribute(this);
    }

}
