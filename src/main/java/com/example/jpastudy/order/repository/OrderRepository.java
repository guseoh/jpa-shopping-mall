package com.example.jpastudy.order.repository;

import com.example.jpastudy.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
