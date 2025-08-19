package com.example.shopapp.service.product;

import com.example.shopapp.Response.product.ProductResponse;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductResponse> getAllProduct(String keyword, Long categoryId, PageRequest pageRequest) {
        Page<Product> products = productRepository.searchProduct(categoryId, keyword, pageRequest);
        return products.map(ProductResponse::fromProduct);
    }

}
