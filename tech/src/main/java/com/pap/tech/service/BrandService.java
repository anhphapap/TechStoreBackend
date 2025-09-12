package com.pap.tech.service;

import com.pap.tech.entity.Brand;
import com.pap.tech.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;

    public List<Brand> getBrandList(){
        return brandRepository.findAll();
    }
}
