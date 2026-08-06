package com.example.jpastudy.order.domain;


import com.example.jpastudy.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 주문 당시 확정 가격
    @Column(name = "order_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal orderPrice;

    @Column(nullable = false)
    private int quantity;

    public OrderItem(
            Order order,
            Product product,
            BigDecimal orderPrice,
            int quantity
    ) {
        validateOrderPrice(orderPrice);
        validateQuantity(quantity);

        this.order = order;
        this.product = product;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    public BigDecimal calculateTotalPrice() {
        return orderPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private static void validateOrderPrice(BigDecimal orderPrice) {
        if (orderPrice == null || orderPrice.signum() < 0) {
            throw new IllegalArgumentException(
                    "주문 가격은 0 이상이어야 합니다."
            );
        }
    }

    private static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "주문 수량은 1 이상이어야 합니다."
            );
        }
    }
}
