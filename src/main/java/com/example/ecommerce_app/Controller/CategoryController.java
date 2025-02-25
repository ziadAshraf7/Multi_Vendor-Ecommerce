package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.Category_Table.ParentCategoryCreationDto;
import com.example.ecommerce_app.Dto.Category_Table.SubCategoryCreationDto;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Services.Category.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<Category>> getSubCategories() {
        return new ResponseEntity<>( categoryService.getSubCategories(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addParentCategory(@Valid @ModelAttribute ParentCategoryCreationDto parentCategoryCreationDto) throws IOException {
        categoryService.addParentCategory(parentCategoryCreationDto);
        return new ResponseEntity<>( "Category Created", HttpStatus.CREATED);
    }

    @PostMapping("/subCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addSubCategory(@Valid @ModelAttribute SubCategoryCreationDto subCategoryCreationDto) throws IOException {
        categoryService.addSubCategory(subCategoryCreationDto);
        return new ResponseEntity<>( "Sub Category Created", HttpStatus.CREATED);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCategory(@RequestParam long categoryId){
        categoryService.deleteCategoryById(categoryId);
        return "Deleted Successfully";
    }

}
