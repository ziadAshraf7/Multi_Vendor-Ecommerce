package com.example.ecommerce_app.Repositery.User;

import com.example.ecommerce_app.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
