package com.pap.tech.mapper;

import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ListProductResponse toListProductResponse(Product res);
}
