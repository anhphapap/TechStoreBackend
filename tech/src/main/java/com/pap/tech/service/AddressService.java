package com.pap.tech.service;

import com.pap.tech.dto.request.AddressRequest;
import com.pap.tech.dto.response.AddressResponse;
import com.pap.tech.entity.Address;
import com.pap.tech.entity.User;
import com.pap.tech.exception.AppException;
import com.pap.tech.exception.ErrorCode;
import com.pap.tech.mapper.AddressMapper;
import com.pap.tech.repository.AddressRepository;
import com.pap.tech.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    UserRepository userRepository;

    public List<AddressResponse> getAddresses(){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        List<Address> list = addressRepository.findByUser_Id(user.getId(), Sort.by(Sort.Direction.DESC, "isdefault"));
        return list.stream().map(addressMapper::addressToAddressResponse).collect(Collectors.toList());
    }

    public void createAddress(AddressRequest address){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Address a = addressMapper.toAddress(address);
        a.setUser(user);
        addressRepository.save(a);
    }
}
