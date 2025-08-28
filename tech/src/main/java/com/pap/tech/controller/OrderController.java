package com.pap.tech.controller;

import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/add")
    public ApiResponse addOrder(@RequestBody OrderRequest orderRequest) {
        orderService.addOrder(orderRequest);
        return ApiResponse.builder()
                .message("Tạo đơn hàng thành công!")
                .build();
    }
}
