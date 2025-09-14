package com.pap.tech.repository;

import com.pap.tech.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByParentIsNull();

    Optional<Category> findById(String id);
}
