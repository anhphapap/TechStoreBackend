package com.pap.tech.mapper;

import com.pap.tech.dto.request.UserCreationRequest;
import com.pap.tech.dto.request.UserUpdateRequest;
import com.pap.tech.dto.response.UserResponse;
import com.pap.tech.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    @Mapping(source = "cart.id", target = "cartId")
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
