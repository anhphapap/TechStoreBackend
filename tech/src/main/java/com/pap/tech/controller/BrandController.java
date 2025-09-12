package com.pap.tech.controller;

import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.entity.Brand;
import com.pap.tech.repository.BrandRepository;
import com.pap.tech.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @GetMapping
    public ApiResponse<List<Brand>> getBrandList(){
        return ApiResponse.<List<Brand>>builder()
                .result(brandService.getBrandList())
                .build();
    }
}
