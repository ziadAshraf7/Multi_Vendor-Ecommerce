package com.example.ecommerce_app.Entity;

import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
@SuperBuilder
@AllArgsConstructor
public class User extends BaseEntity {

    public User(){
        super();
    }

    @Column(name = "user_name" , nullable = false)
    @NotEmpty
    private String userName;

    @Column(name = "email" , nullable = false , unique = true)
    @Email
    @NotEmpty
    private String email;

    @Column(name = "password" , nullable = false)
    @NotEmpty
    private String password;

    @Column(name = "phone" , nullable = false , unique = true)
    @NotEmpty
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRoles userRole;

    @Column(name = "address" , nullable = false )
    @NotEmpty
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vendor" , cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    @JsonIgnore
    private List<VendorProduct> vendor_products;

    @OneToMany(mappedBy = "user" ,cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    private final List<ProductReview> productReviews = new ArrayList<>();

    @OneToMany( fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST , CascadeType.REMOVE})
    private List<Cart> cart;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.REMOVE , CascadeType.PERSIST})
    private List<Order> order;

}
