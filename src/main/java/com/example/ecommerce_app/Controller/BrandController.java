package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Services.Brand.BrandService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/brand")
@AllArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandResponseDto>> getAllBrands(){
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBrand(@Valid @ModelAttribute BrandCreationDto brandCreationDto) throws IOException {
        brandService.addBrand(brandCreationDto);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.CREATED) ;
    }
}
