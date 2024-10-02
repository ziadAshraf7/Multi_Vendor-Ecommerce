package com.example.ecommerce_app.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "brand")
@Builder
@AllArgsConstructor
@Data
public class Brand  {

    public Brand(){
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name" , unique = true , nullable = false)
    @NotEmpty
    private String name;


    @NotNull(message = "image cannot be null")
    @Column(name = "image" , nullable = false)
    private byte[] image;

    @OneToMany(mappedBy = "brand" ,cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JsonIgnore
    List<Product> products;

}
