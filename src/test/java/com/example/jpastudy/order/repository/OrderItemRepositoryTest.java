package com.example.jpastudy.order.repository;

import com.example.jpastudy.member.domain.Member;
import com.example.jpastudy.member.repository.MemberRepository;
import com.example.jpastudy.order.domain.Order;
import com.example.jpastudy.order.domain.OrderItem;
import com.example.jpastudy.order.domain.OrderStatus;
import com.example.jpastudy.product.domain.Product;
import com.example.jpastudy.product.repository.ProductRepository;
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
class OrderRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("주문과 주문 상품의 연관관계를 외래 키로 저장한다")
    void saveOrderAndOrderItem() {
        Member member = memberRepository.save(
                new Member(
                        "홍길동",
                        "order-member@example.com"
                )
        );

        Product product = productRepository.save(
                new Product(
                        "기계식 키보드",
                        new BigDecimal("120000.00"),
                        10
                )
        );

        Order order = orderRepository.save(
                new Order(member)
        );

        OrderItem orderItem = orderItemRepository.save(
                new OrderItem(
                        order,
                        product,
                        product.getPrice(),
                        2
                )
        );

        Long orderId = order.getId();
        Long orderItemId = orderItem.getId();

        entityManager.flush();
        entityManager.clear();

        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow();

        OrderItem foundOrderItem =
                orderItemRepository.findById(orderItemId)
                        .orElseThrow();

        assertThat(foundOrder.getStatus())
                .isEqualTo(OrderStatus.ORDERED);

        assertThat(foundOrder.getMember().getId())
                .isEqualTo(member.getId());

        assertThat(foundOrderItem.getOrder().getId())
                .isEqualTo(orderId);

        assertThat(foundOrderItem.getProduct().getId())
                .isEqualTo(product.getId());

        assertThat(foundOrderItem.getOrderPrice())
                .isEqualByComparingTo("120000.00");

        assertThat(foundOrderItem.getQuantity())
                .isEqualTo(2);

        assertThat(foundOrderItem.calculateTotalPrice())
                .isEqualByComparingTo("240000.00");
    }

    @Test
    @DisplayName("주문 조회 시 회원은 지연 로딩된다")
    void lazyLoadingMember() {
        Member member = memberRepository.save(
                new Member(
                        "김철수",
                        "lazy-member@example.com"
                )
        );

        Order order = orderRepository.save(
                new Order(member)
        );

        Long orderId = order.getId();

        entityManager.flush();
        entityManager.clear();

        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow();

        assertThat(foundOrder.getStatus())
                .isEqualTo(OrderStatus.ORDERED);

        String memberName = foundOrder.getMember().getName();

        assertThat(memberName).isEqualTo("김철수");
    }
}