package com.pap.tech.mapper;

import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.dto.response.ProductResponse;
import com.pap.tech.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ListProductResponse toListProductResponse(Product res);
    void updateProduct(@MappingTarget Product product, ListProductResponse response);
}
