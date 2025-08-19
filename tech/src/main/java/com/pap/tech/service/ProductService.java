package com.pap.tech.service;

import com.pap.tech.entity.Product;
import com.pap.tech.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService{
    int PAGE_SIZE = 16;
    ProductRepository productRepository;

    public Page<Product> getProducts(String sort, int page, String search) {
        Sort sortConfig = Sort.unsorted();

        if (sort != null && !sort.isEmpty()) {
            Sort.Direction direction = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortConfig = Sort.by(direction, "price");
        }

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, sortConfig);

        if (search != null && !search.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        }

        return productRepository.findAll(pageable);
    }


}
