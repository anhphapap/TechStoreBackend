package com.pap.tech.service;

import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.dto.response.ProductResponse;
import com.pap.tech.entity.Product;
import com.pap.tech.entity.ProductAttribute;
import com.pap.tech.entity.ProductImage;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.ProductAttributeMapper;
import com.pap.tech.mapper.ProductImageMapper;
import com.pap.tech.mapper.ProductMapper;
import com.pap.tech.repository.ProductAttributeRepository;
import com.pap.tech.repository.ProductImageRepository;
import com.pap.tech.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService{
    int PAGE_SIZE = 16;
    ProductRepository productRepository;
    ProductImageRepository productImageRepository;
    ProductAttributeRepository productAttributeRepository;
    ProductImageMapper productImageMapper;
    ProductAttributeMapper productAttributeMapper;
    ProductMapper productMapper;

    public Page<ListProductResponse> getProducts(String sort, int page, String search) {
        Sort sortConfig = Sort.unsorted();

        if (sort != null && !sort.isEmpty()) {
            Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortConfig = Sort.by(direction, "price");
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, sortConfig);

        if (search != null && !search.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable).map(productMapper::toListProductResponse);
        }

        return  productRepository.findAll(pageable).map(productMapper::toListProductResponse);
    }

    public List<ListProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toListProductResponse).collect(Collectors.toList());
    }

    public ProductResponse getProduct(String id) {
        Product product = productRepository.findById(id).orElse(null);
        List<ProductImage> listImages = productImageRepository.findByProduct_Id(id, Sort.by(Sort.Direction.ASC, "url"));
        List<ProductAttribute> listAttributes = productAttributeRepository.findByProduct_Id(id);
        return ProductResponse.builder()
                .product(productMapper.toListProductResponse(product))
                .productImages(listImages.stream().map(productImageMapper::toProductImageResponse).collect(Collectors.toList()))
                .productAttributes(listAttributes.stream().map(productAttributeMapper::toProductAttributeResponse).collect(Collectors.toList()))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getProductStats(){
        long total = productRepository.count();
        return Map.of(
                "total", total
        );
    }
}
