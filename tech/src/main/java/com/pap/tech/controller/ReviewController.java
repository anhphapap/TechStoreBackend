package com.pap.tech.controller;

import com.pap.tech.dto.request.OrderRequest;
import com.pap.tech.dto.request.ReviewRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.ReviewResponse;
import com.pap.tech.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;
    @PostMapping("/add")
    public ApiResponse addOrder(@PathVariable String productId,@RequestBody ReviewRequest request) {
        reviewService.addReview(request, productId);
        return ApiResponse.builder()
                .message("Thêm đánh giá thành công!")
                .build();
    }

    @GetMapping
    public ApiResponse<List<ReviewResponse>> getReviews(
            @PathVariable String productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ReviewResponse> reviewPage = reviewService.getReviews(productId, page, size);
        return ApiResponse.<List<ReviewResponse>>builder()
                .result(reviewPage.getContent())
                .page(page)
                .pageSize(reviewPage.getSize())
                .totalPages(reviewPage.getTotalPages())
                .totalCount((int)reviewPage.getTotalElements())
                .build();
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getStats(@PathVariable String productId) {
        return ApiResponse.<Map<String, Object>>builder()
                .result(reviewService.getReviewStats(productId))
                .build();
    }
}
