package com.pap.tech.repository;

import com.pap.tech.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Brand findById(String id);
}
