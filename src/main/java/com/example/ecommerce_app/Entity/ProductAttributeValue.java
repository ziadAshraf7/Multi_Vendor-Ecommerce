package com.example.ecommerce_app.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_attribute_value")
@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductAttributeValue {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private long id;

     @Column(name = "value" , nullable = false)
     @NotEmpty
     private String value;

     @ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
     @JoinColumn(name = "attribute_id")
     private Attribute attribute;

     @ManyToOne(fetch = FetchType.LAZY , cascade = { CascadeType.PERSIST})
     @JoinColumn(name = "product_id")
     private Product product;
}
