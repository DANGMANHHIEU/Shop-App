package com.example.shopapp.repositories;

import com.example.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products p WHERE (:categoryId IS null OR :categoryId = 0 or p.category_id = :categoryId )" +
            "AND ( :keyword IS NULL OR :keyword ='' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)", nativeQuery = true)
    Page<Product> searchProduct(@Param("categoryId") Long categoryId,
                                @Param("keyword") String keyword, Pageable pageable);

}
