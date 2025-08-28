package com.pap.tech.repository;

import com.pap.tech.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order findOrderByVnpTxnref(String vnpTxnref);
}
