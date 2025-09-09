package com.pap.tech.repository;

import com.pap.tech.entity.OrderDetail;
import com.pap.tech.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByOrderId(String orderId);

    @Query("SELECT od.product.id, SUM(od.quantity) as totalSold " +
            "FROM OrderDetail od " +
            "GROUP BY od.product.id " +
            "ORDER BY totalSold DESC LIMIT 5")
    List<Object[]> findTop5BestSellingProducts();

    boolean existsByOrder_User_IdAndProduct_IdAndOrder_Status(String orderUserId, String productId, OrderStatus orderStatus);
}
