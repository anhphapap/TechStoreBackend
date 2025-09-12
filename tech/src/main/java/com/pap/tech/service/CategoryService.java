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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper mapper;
    ProductRepository productRepository;
    ProductMapper  productMapper;

    public List<CategoryResponse> getCategoryTree(){
        return mapper.toCategoryResponseList(categoryRepository.findByParentIsNull());
    }

    public String getCategoryName(String id){
        return categoryRepository.findById(id).getName();
    }

    public Page<ListProductResponse> getProductsByCategorySlug(
            String slug,
            List<String> brandIds,
            Long minPrice,
            Long maxPrice,
            Pageable pageable
    ) {
        Category category = categoryRepository.findById(slug);

        List<String> categoryIds = getAllCategoryIds(category);

        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(categoryIn(categoryIds));

        if (brandIds != null && !brandIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("brand").get("id").in(brandIds));
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(priceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            spec = spec.and(priceGreater(minPrice));
        } else if (maxPrice != null) {
            spec = spec.and(priceLess(maxPrice));
        }

        return productRepository.findAll(spec, pageable).map(productMapper::toListProductResponse);
    }

    private List<String> getAllCategoryIds(Category category) {
        List<String> ids = new ArrayList<>();
        ids.add(category.getId());
        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                ids.addAll(getAllCategoryIds(child));
            }
        }
        return ids;
    }

    private Specification<Product> categoryIn(List<String> categoryIds) {
        return (root, query, cb) -> root.get("category").get("id").in(categoryIds);
    }

    private Specification<Product> hasBrand(String brandId) {
        return (root, query, cb) -> cb.equal(root.get("brand").get("id"), brandId);
    }

    private Specification<Product> priceBetween(Long min, Long max) {
        return (root, query, cb) -> cb.between(root.get("price"), min, max);
    }

    private Specification<Product> priceGreater(Long min) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), min);
    }

    private Specification<Product> priceLess(Long max) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), max);
    }
}
