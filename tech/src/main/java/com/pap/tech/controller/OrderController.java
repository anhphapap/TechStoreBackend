package com.pap.tech.controller;

import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.request.UpdateRepaidRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.ListOrderResponse;
import com.pap.tech.dto.response.OrderResponse;
import com.pap.tech.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/add")
    public ApiResponse<String> addOrder(@RequestBody OrderRequest orderRequest) {
        String id = orderService.addOrder(orderRequest);
        return ApiResponse.<String>builder()
                .message("Tạo đơn hàng thành công!")
                .result(id)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ListOrderResponse>> findAllOrders(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "") String status) {
        Page<ListOrderResponse> responses = orderService.getOrders(status, page);
        return ApiResponse.<List<ListOrderResponse>>builder()
                .result(responses.getContent())
                .page(page)
                .pageSize(responses.getSize())
                .totalPages(responses.getTotalPages())
                .totalCount((int)responses.getTotalElements())
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> findOrderById(@PathVariable("orderId") String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderDetail(orderId))
                .build();
    }

    @PutMapping("/repaid/{orderId}")
    public ApiResponse updateRepaid(@PathVariable("orderId") String orderId, @RequestBody UpdateRepaidRequest request) {
        System.out.println(request.getVnpTxnref());
        orderService.updateRepaidVnpTxnRef(orderId, request.getVnpTxnref());
        return ApiResponse.builder()
                .message("Cập nhật thành công")
                .build();
    }

    @PutMapping("/{orderId}")
    public ApiResponse updateOrderStatus(@PathVariable("orderId") String orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ApiResponse.builder()
                .message("Cập nhật trạng thái đơn hàng thành công !")
                .build();
    }
}
