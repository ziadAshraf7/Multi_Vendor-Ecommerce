package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "product_attribute_value")
@Builder
@AllArgsConstructor
public class Product_Attribute_Value {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private long id;

     @Column(name = "value" , nullable = false)
     @NotEmpty
     private String value;


     @ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
     @JoinColumn(name = "sub_category_attribute_id")
     private Sub_Category_Attribute subCategoryAttribute;

}
