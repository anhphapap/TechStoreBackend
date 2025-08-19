package com.pap.tech.controller;

import com.pap.tech.dto.request.AddToCartRequest;
import com.pap.tech.dto.request.CartItemRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.CartItemResponse;
import com.pap.tech.entity.CartItem;
import com.pap.tech.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {
    CartService cartService;

    @PostMapping("/add")
    public ApiResponse addItem(@RequestBody AddToCartRequest request) {
        cartService.addItem(request.getCartId(), request.getProductId(), request.getQuantity());
        return ApiResponse.builder()
                .message("Thêm sản phẩm vào giỏ thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getCartItems() {
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartService.getCart())
                .build();
    }

    @PostMapping("/merge")
    public ApiResponse<List<CartItemResponse>> mergeCartItems(@RequestBody List<CartItemRequest> requests) {
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartService.mergeCart(requests))
                .build();
    }
}
