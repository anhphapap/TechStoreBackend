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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String addOrUpdateAddress(AddressRequest address){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(Boolean.TRUE.equals(address.getIsdefault())){
            addressRepository.updateIsDefaultFalseByUser(user.getId());
        }
        if(address.getId() == null || address.getId().isBlank()){
            Address a = addressMapper.toAddress(address);
            a.setUser(user);
            addressRepository.save(a);
            return "Thêm địa chỉ thành công";
        }else{
            Address a = addressRepository.findById(address.getId()).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
            if(!a.getUser().getId().equals(user.getId())){
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
            addressMapper.updateUser(a, address);
            addressRepository.save(a);
            return "Cập nhật địa chỉ thành công";
        }
    }

    public AddressResponse getAddress(String id){
        Address a = addressRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        return addressMapper.addressToAddressResponse(a);
    }

    public AddressResponse getDefaultAddress(){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Address a = addressRepository.findDefaultByUserId(user.getId());
        return addressMapper.addressToAddressResponse(a);
    }

    public void deleteAddress(String id){
        addressRepository.deleteById(id);
    }
}
