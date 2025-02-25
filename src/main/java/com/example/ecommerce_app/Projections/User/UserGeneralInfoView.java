package com.example.ecommerce_app.Projections.User;

import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserGeneralInfoView {

    long Id;

    String email;

    UserRoles UserRole;

}
