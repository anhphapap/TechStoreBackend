package com.pap.tech.repository;

import com.pap.tech.entity.Order;
import com.pap.tech.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT MONTH(o.orderdate), SUM(o.totalamount) " +
            "FROM Order o " +
            "WHERE YEAR(o.orderdate) = :year AND o.status = 'DELIVERED' " +
            "GROUP BY MONTH(o.orderdate)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);
    Order findOrderByVnpTxnref(String vnpTxnref);
    @Query("SELECT o FROM Order o " +
            "WHERE o.user.username = :username " +
            "AND (:status IS NULL OR o.status = :status) " +
            "ORDER BY o.orderdate DESC")
    Page<Order> findByUsernameAndOptionalStatus(@Param("username") String username,
                                              @Param("status") OrderStatus status,
                                              Pageable pageable);

    Order findOrderById(String id);

    Page<Order> findAll(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:query IS NULL OR LOWER(o.user.fullname) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR o.id LIKE CONCAT('%', :query, '%'))")
    Page<Order> searchOrders(@Param("status") OrderStatus status,
                             @Param("query") String query,
                             Pageable pageable);

    long countByStatus(OrderStatus status);

    @Query("SELECT SUM(o.totalamount) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal sumRevenue();

    @Query("SELECT DATE(o.orderdate) as date, SUM(o.totalamount) as amount " +
            "FROM Order o WHERE o.status = 'DELIVERED' " +
            "GROUP BY DATE(o.orderdate) ORDER BY DATE(o.orderdate) DESC")
    List<Map<String, Object>> findDailyRevenueLast7Days();

    Page<Order> findOrderByStatus(OrderStatus status, Pageable pageable);
}
