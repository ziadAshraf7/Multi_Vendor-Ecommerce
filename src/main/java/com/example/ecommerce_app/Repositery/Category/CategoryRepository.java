package com.example.ecommerce_app.Repositery.Category;


import com.example.ecommerce_app.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {
    Category findByName(String name);
}
