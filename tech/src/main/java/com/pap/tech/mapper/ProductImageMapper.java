package com.pap.tech.mapper;

import com.pap.tech.dto.response.ProductImageResponse;
import com.pap.tech.entity.ProductImage;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Sort;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageResponse toProductImageResponse(ProductImage productImage);
}
