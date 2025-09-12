package com.pap.tech.controller;

import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.CategoryResponse;
import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.dto.response.ProductResponse;
import com.pap.tech.entity.Product;
import com.pap.tech.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @GetMapping("/tree")
    public ApiResponse<List<CategoryResponse>> tree() {
        return  ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getCategoryTree())
                .build();
    }

    @GetMapping("/{slug}")
    public ApiResponse<List<ListProductResponse>> getProductsByCategory(
            @PathVariable String slug,
            @RequestParam(required = false) List<String> brandIds,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "1") int page
    ) {
        Sort sortConfig = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortConfig = Sort.by(direction, "price");
        }
        Pageable pageable = PageRequest.of(page - 1, 16, sortConfig);
        Page<ListProductResponse> res = categoryService.getProductsByCategorySlug(slug, brandIds, minPrice, maxPrice, pageable);

        return ApiResponse.<List<ListProductResponse>>builder()
                .result(res.getContent())
                .categoryName(categoryService.getCategoryName(slug))
                .page(page)
                .pageSize(res.getSize())
                .totalPages(res.getTotalPages())
                .totalCount((int)res.getTotalElements())
                .build();
    }
}
