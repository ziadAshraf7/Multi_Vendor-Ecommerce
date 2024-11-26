package com.example.ecommerce_app.Dto.User;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDetails implements Serializable {

    private String userName;

    private String email;

    private String phoneNumber;

    private String address;

    private String userRole;

    public List<GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(userRole));
    }

}
