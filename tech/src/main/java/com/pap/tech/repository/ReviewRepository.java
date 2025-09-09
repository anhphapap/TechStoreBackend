package com.pap.tech.repository;

import com.pap.tech.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Page<Review> findByProductId(String productId, Pageable pageable);
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.rating")
    List<Object[]> countStarsByProductId(@Param("productId") String productId);

    boolean existsByUser_IdAndProduct_Id(String userId, String productId);

    Page<Review> findByProduct_Id(String productId, Pageable pageable);
}
