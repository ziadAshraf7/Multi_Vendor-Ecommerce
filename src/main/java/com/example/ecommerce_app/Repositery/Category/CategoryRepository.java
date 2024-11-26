package com.example.ecommerce_app.Repositery.Category;


import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Projections.Category.CategoryIdsGeneralInfoView;
import com.example.ecommerce_app.Projections.User.UserGeneralInfoInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category , Long> {

    Category findByName(String name);

    @Query("""
            SELECT new com.example.ecommerce_app.Projections.Category
            .CategoryIdsGeneralInfoView(c.id , c.parentCategory.id)
             FROM Category c WHERE c.id = :id
            """ )
    CategoryIdsGeneralInfoView findGeneralInfo(@Param("id") long id );
}
