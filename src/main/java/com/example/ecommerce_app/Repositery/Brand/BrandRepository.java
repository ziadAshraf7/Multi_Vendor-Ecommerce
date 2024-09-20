package com.example.ecommerce_app.Repositery.Brand;


import com.example.ecommerce_app.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand , Long> {


    Brand findByName(String brandName);

}
