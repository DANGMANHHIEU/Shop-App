package com.example.shopapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    /**
     * Lấy toàn bộ sản phẩm
     **/
    @GetMapping("/all")
    public ResponseEntity<?> getAllProduct(@RequestParam("page") int page,
                                           @RequestParam("limit") int limit) {
        return ResponseEntity.ok("getProducts here");
    }

    /**
     * Lấy 1 sản phẩm
     **/
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String productId ){
        return ResponseEntity.ok("Product with ID: " + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String productId){
        return ResponseEntity.ok(String.format("Product with id = %s deleted successfully", productId));
    }
}
