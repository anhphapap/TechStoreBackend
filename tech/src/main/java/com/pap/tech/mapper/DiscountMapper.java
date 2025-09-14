package com.pap.tech.mapper;

import com.pap.tech.dto.request.DiscountRequest;
import com.pap.tech.entity.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    Discount toDiscount(DiscountRequest discountRequest);
    void updateDiscount(@MappingTarget Discount discount, DiscountRequest request);
}
