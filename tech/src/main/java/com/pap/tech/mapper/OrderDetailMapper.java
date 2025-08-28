package com.pap.tech.mapper;

import com.pap.tech.dto.request.OrderDetailRequest;
import com.pap.tech.entity.OrderDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail toOrderDetail(OrderDetailRequest request);
}
