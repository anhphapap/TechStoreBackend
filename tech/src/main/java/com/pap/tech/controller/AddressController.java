package com.pap.tech.controller;

import com.pap.tech.dto.request.AddressRequest;
import com.pap.tech.dto.response.AddressResponse;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressController {
    AddressService addressService;

    @GetMapping
    public ApiResponse<List<AddressResponse>> getAddresses(){
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressService.getAddresses())
                .build();
    }

    @PostMapping
    public ApiResponse addAddress(@RequestBody AddressRequest request){
        addressService.createAddress(request);
        return ApiResponse.builder()
                .message("Thêm địa chỉ thành công")
                .build();
    }
}
