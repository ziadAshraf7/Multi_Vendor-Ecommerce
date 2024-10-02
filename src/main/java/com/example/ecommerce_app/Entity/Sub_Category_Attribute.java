package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sub_category_attribute")
@Data
@Builder
@AllArgsConstructor
public class Sub_Category_Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true , nullable = false)
    @NotEmpty
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JoinTable(
            name = "sub_category_attributes",
            joinColumns = { @JoinColumn(name = "sub_category_attribute_id") },
            inverseJoinColumns = { @JoinColumn(name = "sub_category_id") }
    )
    private final Set<Category> subCategories;

    @OneToMany(mappedBy = "subCategoryAttribute" , cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    private final List<Product_Attribute_Value> values;


    public void addValue(Product_Attribute_Value productAttributeValue){
        values.add(productAttributeValue);
    }

}
