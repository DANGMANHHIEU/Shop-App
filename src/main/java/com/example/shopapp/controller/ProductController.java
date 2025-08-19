package com.example.shopapp.controller;

import com.example.shopapp.Response.ResponseObject;
import com.example.shopapp.Response.product.ProductListResponse;
import com.example.shopapp.Response.product.ProductResponse;
import com.example.shopapp.service.product.ProductRedisService;
import com.example.shopapp.service.product.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRedisService productRedisService;

    private final ProductService productService;

    /**
     * Lấy toàn bộ sản phẩm
     **/
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllProduct(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "category_id",required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0",required = false) int page,
            @RequestParam(name = "limit",defaultValue = "10", required = false) int limit) throws JsonProcessingException {

        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        int totalPages = 0;

        List<ProductResponse> productResponses = productRedisService
                .getAllProducts(keyword, categoryId, pageRequest);

        if (productResponses!=null && !productResponses.isEmpty()) {
            totalPages = productResponses.get(0).getTotalPages();
        }

        if(productResponses == null){
            Page<ProductResponse> productPage = productService
                    .getAllProduct(keyword, categoryId, pageRequest);

            // Lấy tổng số trang
            totalPages = productPage.getTotalPages();

            productResponses = productPage.getContent();

            // Bổ sung totalPages vào các đối tượng ProductResponse
            for (ProductResponse product : productResponses) {
                product.setTotalPages(totalPages);
            }

            productRedisService.saveAllProduct(
                    productResponses,
                    keyword,
                    categoryId,
                    pageRequest
            );
        }
        ProductListResponse productListResponse = ProductListResponse
                .builder()
                .products(productResponses)
                .totalPages(totalPages)
                .build();
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Get products successfully")
                .status(HttpStatus.OK)
                .data(productListResponse)
                .build());
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
