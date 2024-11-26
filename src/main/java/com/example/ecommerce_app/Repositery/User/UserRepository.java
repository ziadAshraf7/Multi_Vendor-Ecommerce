package com.example.ecommerce_app.Repositery.User;

import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Projections.User.UserGeneralInfoInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT new com.example.ecommerce_app.Projections.User
            .UserGeneralInfoInfoView(u.id , u.email , u.userRole)
             FROM User u WHERE u.id = :id
            """ )
    UserGeneralInfoInfoView findGeneralInfo(@Param("id") long id );
}
