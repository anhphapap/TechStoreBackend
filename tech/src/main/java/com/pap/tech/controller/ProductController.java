package com.pap.tech.controller;

import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.entity.Product;
import com.pap.tech.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    ProductService  productService;

    @GetMapping
    ApiResponse<List<Product>>  getProducts( @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "") String sort,
                                             @RequestParam(defaultValue = "") String search){
        Page<Product> productPage = productService.getProducts(sort, page, search);
        return ApiResponse.<List<Product>>builder()
                .result(productPage.getContent())
                .page(page)
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalCount((int)productPage.getTotalElements())
                .build();
    }


}
