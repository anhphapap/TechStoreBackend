package com.pap.tech.service;

import com.pap.tech.dto.request.ProductRequest;
import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.dto.response.ProductAttributeResponse;
import com.pap.tech.dto.response.ProductResponse;
import com.pap.tech.entity.*;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.ProductAttributeMapper;
import com.pap.tech.mapper.ProductImageMapper;
import com.pap.tech.mapper.ProductMapper;
import com.pap.tech.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService{
    int PAGE_SIZE = 6;
    ProductRepository productRepository;
    ProductImageRepository productImageRepository;
    ProductAttributeRepository productAttributeRepository;
    ProductImageMapper productImageMapper;
    ProductAttributeMapper productAttributeMapper;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    AttributeRepository attributeRepository;

    @Transactional
    public Product createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
        }

        Product product = Product.builder()
                .id(request.getProduct().getId())
                .name(request.getProduct().getName())
                .price(request.getProduct().getPrice())
                .image(request.getProduct().getImage())
                .category(category)
                .brand(brand)
                .avgRating(0)
                .ratingCount(0)
                .build();

        if (request.getProductImages() != null) {
            List<ProductImage> images = request.getProductImages().stream()
                    .map(img -> ProductImage.builder()
                            .url(img.getUrl())
                            .type(img.getType())
                            .product(product)
                            .build())
                    .toList();
            product.setProductImages(images);
        }

        if (request.getProductAttributes() != null) {
            List<ProductAttribute> attrs = request.getProductAttributes().stream()
                    .map(attr -> ProductAttribute.builder()
                            .attribute(attributeRepository.findById(attr.getAttributeId()).orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND)))
                            .value(attr.getValue())
                            .product(product)
                            .build())
                    .toList();
            product.setProductAttributes(attrs);
        }

        return productRepository.save(product);
    }

    @Transactional
    public void updateProduct(String id, ProductResponse req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, req.getProduct());

        if (req.getCategoryId() != null && product.getCategory().getId() != req.getCategoryId() ) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATE_NOT_FOUND));
            product.setCategory(category);
        }

        if (req.getBrandId() != null && !req.getBrandId().isEmpty() && product.getBrand().getId() != req.getBrandId() ) {
            Brand brand = brandRepository.findById(req.getBrandId())
                    .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
            product.setBrand(brand);
        }

        product.getProductImages().clear();
        List<ProductImage> images = req.getProductImages().stream()
                .map(img -> ProductImage.builder()
                        .url(img.getUrl())
                        .type(img.getType())
                        .product(product)
                        .build())
                .toList();
        product.getProductImages().addAll(images);

        Map<String, ProductAttribute> currentMap = product.getProductAttributes().stream()
                .collect(Collectors.toMap(a -> a.getAttribute().getId(), a -> a));

        List<ProductAttribute> newAttrs = new ArrayList<>();

        for (ProductAttributeResponse attrReq : req.getProductAttributes()) {
            ProductAttribute existing = currentMap.get(attrReq.getAttributeId());
            if (existing != null) {
                existing.setValue(attrReq.getValue());
                newAttrs.add(existing);
            } else {
                Attribute attr = attributeRepository.findById(attrReq.getAttributeId())
                        .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
                newAttrs.add(ProductAttribute.builder()
                        .product(product)
                        .attribute(attr)
                        .value(attrReq.getValue())
                        .build());
            }
        }

        product.getProductAttributes().clear();
        product.getProductAttributes().addAll(newAttrs);

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.delete(product);
    }


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
                .categoryId(product.getCategory().getId())
                .brandId(product.getBrand().getId())
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
