package com.pap.tech.mapper;

import com.pap.tech.dto.response.CartItemResponse;
import com.pap.tech.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.image", target = "productImage")
    @Mapping(source = "product.price", target = "productPrice")
    CartItemResponse toCartItem(CartItem cartItem);
}
