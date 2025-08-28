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

    @GetMapping("/default")
    public ApiResponse<AddressResponse> getDefaultAddress(){
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.getDefaultAddress())
                .build();
    }

    @PostMapping
    public ApiResponse addOrUpdateAddress(@RequestBody AddressRequest request){
        String msg = addressService.addOrUpdateAddress(request);
        return ApiResponse.builder()
                .message(msg)
                .build();
    }

    @GetMapping("/{addressId}")
    public ApiResponse<AddressResponse> getAddress(@PathVariable("addressId") String addressId){
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.getAddress(addressId))
                .build();
    }

    @DeleteMapping("/{addressId}")
    public ApiResponse deleteAddress(@PathVariable("addressId") String addressId){
        addressService.deleteAddress(addressId);
        return ApiResponse.builder()
                .message("Địa chỉ đã được xóa")
                .build();
    }
}
