package com.pap.tech.repository;

import com.pap.tech.entity.Order;
import com.pap.tech.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order findOrderByVnpTxnref(String vnpTxnref);
    @Query("SELECT o FROM Order o " +
            "WHERE o.user.username = :username " +
            "AND (:status IS NULL OR o.status = :status) " +
            "ORDER BY o.orderdate DESC")
    Page<Order> findByUsernameAndOptionalStatus(@Param("username") String username,
                                              @Param("status") OrderStatus status,
                                              Pageable pageable);

    Order findOrderById(String id);
}
