package com.pap.tech.mapper;

import com.pap.tech.dto.response.ProductAttributeResponse;
import com.pap.tech.entity.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {
    @Mapping(source = "attribute.name", target = "name")
    ProductAttributeResponse toProductAttributeResponse(ProductAttribute productAttribute);
}
