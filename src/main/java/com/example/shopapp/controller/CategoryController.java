package com.example.shopapp.controller;

import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories/category")
public class CategoryController {

    @Autowired
    public CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllProductByCategory(@PathVariable Long id) {
        List<Product> products = categoryRepository.getProductList(id);
        return ResponseEntity.ok(products);
    }
}
