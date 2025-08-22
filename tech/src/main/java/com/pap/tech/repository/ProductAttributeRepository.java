package com.pap.tech.repository;

import com.pap.tech.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, String> {
    List<ProductAttribute> findByProduct_Id(String productId);
}
