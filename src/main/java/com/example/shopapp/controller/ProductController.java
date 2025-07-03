package com.example.shopapp.controller;

import com.example.shopapp.Response.product.ProductResponse;
import com.example.shopapp.service.product.ProductRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRedisService productRedisService;

    /**
     * Lấy toàn bộ sản phẩm
     **/
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllProduct(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "category_id",required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "limit",defaultValue = "10", required = false) int limit) {

        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );

        List<ProductResponse> productResponses = productRedisService
                .getAllProducts(keyword, categoryId, pageRequest);
        return ResponseEntity.ok("getProducts here");
    }

    /**
     * Lấy 1 sản phẩm
     **/
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String productId) {
        return ResponseEntity.ok("Product with ID: " + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String productId) {
        return ResponseEntity.ok(String.format("Product with id = %s deleted successfully", productId));
    }
}
