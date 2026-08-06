package com.example.jpastudy.product.repository;


import com.example.jpastudy.product.domain.Product;
import com.example.jpastudy.product.domain.ProductStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("상품을 저장하고 DB에서 다시 조회한다")
    void saveAndFind() {
        Product product = new Product(
                "기계식 키보드",
                new BigDecimal("120000.00"),
                10
        );

        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        entityManager.flush();
        entityManager.clear();

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow();

        assertThat(foundProduct.getName()).isEqualTo("기계식 키보드");
        assertThat(foundProduct.getPrice())
                .isEqualByComparingTo("120000.00");
        assertThat(foundProduct.getStock()).isEqualTo(10);
        assertThat(foundProduct.getStatus())
                .isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("재고를 모두 차감하면 상품 상태가 품절로 변경된다")
    void decreaseStock() {
        Product product = productRepository.save(
                new Product(
                        "무선 마우스",
                        new BigDecimal("50000.00"),
                        5
                )
        );

        product.decreaseStock(5);

        entityManager.flush();
        entityManager.clear();

        Product foundProduct = productRepository.findById(product.getId())
                .orElseThrow();

        assertThat(foundProduct.getStock()).isZero();
        assertThat(foundProduct.getStatus())
                .isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("품절 상품의 재고를 추가하면 판매 중 상태로 변경된다")
    void increaseStock() {
        Product product = productRepository.save(
                new Product(
                        "USB 허브",
                        new BigDecimal("35000.00"),
                        0
                )
        );

        assertThat(product.getStatus())
                .isEqualTo(ProductStatus.SOLD_OUT);

        product.increaseStock(3);

        entityManager.flush();
        entityManager.clear();

        Product foundProduct = productRepository.findById(product.getId())
                .orElseThrow();

        assertThat(foundProduct.getStock()).isEqualTo(3);
        assertThat(foundProduct.getStatus())
                .isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("보유 재고보다 많은 수량은 차감할 수 없다")
    void decreaseStockOverCurrentStock() {
        Product product = new Product(
                "노트북 거치대",
                new BigDecimal("40000.00"),
                3
        );

        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> product.decreaseStock(4)
                )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상품 재고가 부족합니다.");
    }
}