package com.pap.tech.mapper;

import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.request.UpdateOrderRequest;
import com.pap.tech.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest order);
    void updateOrder(@MappingTarget Order order, UpdateOrderRequest request);
}
