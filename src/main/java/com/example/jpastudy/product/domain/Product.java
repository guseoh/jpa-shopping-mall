package com.example.jpastudy.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Product(String name, BigDecimal price, int stock) {

        validatePrice(price);
        validateStock(stock);

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = determineStatus(stock);
        this.createdAt = LocalDateTime.now();
    }

    public void increaseStock(int quantity) {
        validateQuantity(quantity);

        this.stock += quantity;

        if (this.status == ProductStatus.SOLD_OUT) {
            this.status = ProductStatus.ON_SALE;
        }
    }

    public void decreaseStock(int quantity) {
        validateQuantity(quantity);

        if (this.stock < quantity) {
            throw new IllegalStateException("상품 재고가 부족합니다.");
        }

        this.stock -= quantity;

        if (this.stock == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("상품 가격은 0 이상이어야 합니다.");
        }
    }

    private static void validateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("상품 재고는 0 이상이어야 합니다.");
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
    }

    private static ProductStatus determineStatus(int stock) {
        return stock == 0
                ? ProductStatus.SOLD_OUT
                : ProductStatus.ON_SALE;
    }
}

/*
    BigDecimal
    - 금액은 double나 float 대신 BigDecimal을 사용한다.
    - 정확한 소수 계산

    @Column(nullable = false, precision = 12, scale = 2)
    - precision = 12: 전체 자릿수
    - scale = 2: 소수점 이하 자릿수
 */