package com.example.jpastudy.product.repository;

import com.example.jpastudy.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
