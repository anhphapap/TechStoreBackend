package com.pap.tech.repository;

import com.pap.tech.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByParentIsNull();

//    @Query("""
//        select distinct c
//        from Category c
//        left join fetch c.children
//        where c.parent is null
//    """)
//    List<Category> findRootWithChildren();
}
