package com.pap.tech.service;

import com.pap.tech.dto.response.CategoryResponse;
import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.entity.Brand;
import com.pap.tech.entity.Category;
import com.pap.tech.entity.Product;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.CategoryMapper;
import com.pap.tech.mapper.ProductMapper;
import com.pap.tech.repository.BrandRepository;
import com.pap.tech.repository.CategoryRepository;
import com.pap.tech.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoryTree() {
        List<Category> all = categoryRepository.findAll();

        Map<String, CategoryResponse> nodeMap = new HashMap<>();
        for (Category c : all) {
            nodeMap.put(c.getId(),
                    CategoryResponse.builder()
                            .id(c.getId())
                            .name(c.getName())
                            .children(new ArrayList<>())
                            .build()
            );
        }

        List<CategoryResponse> roots = new ArrayList<>();
        for (Category c : all) {
            CategoryResponse node = nodeMap.get(c.getId());

            if (c.getParentId() == null) {
                roots.add(node);
            } else {
                CategoryResponse parent = nodeMap.get(c.getParentId());
                if (parent != null) parent.getChildren().add(node);
                else roots.add(node); // phòng trường hợp parent thiếu
            }
        }

        return roots;
    }

    @Transactional(readOnly = true)
    public String getCategoryName(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATE_NOT_FOUND))
                .getName();
    }

    @Transactional(readOnly = true)
    public Page<ListProductResponse> getProductsByCategorySlug(
            String slug,
            List<String> brandIds,
            Long minPrice,
            Long maxPrice,
            Pageable pageable
    ) {
        // đảm bảo category tồn tại
        Category category = categoryRepository.findById(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATE_NOT_FOUND));

        // lấy toàn bộ category 1 lần, build danh sách id con theo parentId
        List<Category> all = categoryRepository.findAll();

        Map<String, List<String>> childrenByParent = new HashMap<>();
        for (Category c : all) {
            String parentId = c.getParentId();
            if (parentId != null) {
                childrenByParent.computeIfAbsent(parentId, k -> new ArrayList<>())
                        .add(c.getId());
            }
        }

        // BFS/DFS lấy toàn bộ subtree ids
        List<String> categoryIds = collectSubtreeIds(category.getId(), childrenByParent);

        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and((root, query, cb) -> root.get("category").get("id").in(categoryIds));

        if (brandIds != null && !brandIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("brand").get("id").in(brandIds));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        return productRepository.findAll(spec, pageable)
                .map(productMapper::toListProductResponse);
    }

    private List<String> collectSubtreeIds(String rootId, Map<String, List<String>> childrenByParent) {
        List<String> result = new ArrayList<>();
        ArrayList<String> stack = new ArrayList<>();
        stack.add(rootId);

        while (!stack.isEmpty()) {
            String current = stack.remove(stack.size() - 1);
            result.add(current);

            List<String> kids = childrenByParent.get(current);
            if (kids != null && !kids.isEmpty()) {
                stack.addAll(kids);
            }
        }
        return result;
    }
}
