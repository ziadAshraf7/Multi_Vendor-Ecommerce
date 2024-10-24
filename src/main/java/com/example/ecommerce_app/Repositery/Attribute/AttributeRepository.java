package com.example.ecommerce_app.Repositery.Attribute;

import com.example.ecommerce_app.Entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute , Long> {

    @Query("SELECT a FROM Attribute a WHERE a.name = :name")
    Attribute getEntityByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Attribute a WHERE a.name = :name")
    void deleteEntityByName(@Param("name") String name);

}
