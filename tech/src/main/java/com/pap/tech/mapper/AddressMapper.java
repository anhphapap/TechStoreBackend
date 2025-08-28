package com.pap.tech.mapper;

import com.pap.tech.dto.request.AddressRequest;
import com.pap.tech.dto.response.AddressResponse;
import com.pap.tech.entity.Address;
import com.pap.tech.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse addressToAddressResponse(Address address);
    Address toAddress(AddressRequest request);
    void updateUser(@MappingTarget Address address, AddressRequest request);
}
