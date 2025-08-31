package com.pap.tech.mapper;

import com.pap.tech.dto.request.OrderDetailRequest;
import com.pap.tech.dto.response.OrderDetailResponse;
import com.pap.tech.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail toOrderDetail(OrderDetailRequest request);
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.image", target = "productImage")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}
