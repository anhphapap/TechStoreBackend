package com.pap.tech.controller;

import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.request.UpdateRepaidRequest;
import com.pap.tech.dto.response.AdminOrderResponse;
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
import java.util.Map;

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
    public ApiResponse<List<ListOrderResponse>> findUserOrders(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "") String status) {
        Page<ListOrderResponse> responses = orderService.getUserOrders(status, page);
        return ApiResponse.<List<ListOrderResponse>>builder()
                .result(responses.getContent())
                .page(page)
                .pageSize(responses.getSize())
                .totalPages(responses.getTotalPages())
                .totalCount((int)responses.getTotalElements())
                .build();
    }

    @GetMapping("/admin")
    public ApiResponse<List<AdminOrderResponse>> findAllOrders(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "") String status,
                                                               @RequestParam(defaultValue = "") String query) {
        Page<AdminOrderResponse> responses = orderService.getOrders(status, page, query);
        return ApiResponse.<List<AdminOrderResponse>>builder()
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

    @GetMapping("/stats")
    public ApiResponse<Map<String, Long>> getOrderStats(){
        return ApiResponse.<Map<String, Long>>builder()
                .result(orderService.getOrderStats())
                .build();
    }

    @GetMapping("/revenue")
    public ApiResponse<Map<String, Object>> getRevenueStats(){
        return ApiResponse.<Map<String, Object>>builder()
                .result(orderService.getRevenueStats())
                .build();
    }
}
