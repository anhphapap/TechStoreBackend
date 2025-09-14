package com.pap.tech.controller;

import com.pap.tech.dto.request.DiscountRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.entity.Discount;
import com.pap.tech.service.DiscountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountController {
    DiscountService discountService;

    @GetMapping("/apply")
    public ApiResponse<BigDecimal> applyDiscount(@RequestParam String code, @RequestParam BigDecimal total) {
        return ApiResponse.<BigDecimal>builder()
                .result(discountService.applyDiscount(code, total))
                .build();
    }

    @GetMapping
    public ApiResponse<List<Discount>> getDiscounts() {
        return ApiResponse.<List<Discount>>builder()
                .result(discountService.getDiscounts())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Discount> getDiscount(@PathVariable String id) {
        return ApiResponse.<Discount>builder()
                .result(discountService.getDiscount(id))
                .build();
    }

    @GetMapping("/{id}/active")
    public ApiResponse activeDiscount(@PathVariable String id) {
        discountService.activeDiscount(id);
        return ApiResponse.builder()
                .message("Cập nhập thành công")
                .build();
    }

    @PostMapping
    public ApiResponse addDiscount(@RequestBody DiscountRequest discount) {
        discountService.addDiscount(discount);
        return ApiResponse.builder()
                .message("Thêm mã giảm giá thành công")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse updateDiscount(@PathVariable String id, @RequestBody DiscountRequest discount) {
        discountService.updateDiscount(id, discount);
        return ApiResponse.builder()
                .message("Cập nhập thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteDiscount(@PathVariable String id) {
        discountService.deleteDiscount(id);
        return ApiResponse.builder()
                .message("Cập nhập thành công")
                .build();
    }
}
