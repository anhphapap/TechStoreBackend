package com.pap.tech.controller;

import com.pap.tech.dto.request.ProductRequest;
import com.pap.tech.dto.response.ApiResponse;
import com.pap.tech.dto.response.ListProductResponse;
import com.pap.tech.dto.response.ProductResponse;
import com.pap.tech.entity.Product;
import com.pap.tech.enums.OrderStatus;
import com.pap.tech.repository.OrderDetailRepository;
import com.pap.tech.repository.OrderRepository;
import com.pap.tech.repository.ReviewRepository;
import com.pap.tech.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    ProductService  productService;
    OrderDetailRepository orderDetailRepository;
    ReviewRepository reviewRepository;

    @GetMapping
    ApiResponse<List<ListProductResponse>>  getProducts(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "") String sort,
                                                        @RequestParam(defaultValue = "") String search){
        Page<ListProductResponse> productPage = productService.getProducts(sort, page, search);
        return ApiResponse.<List<ListProductResponse>>builder()
                .result(productPage.getContent())
                .page(page)
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .totalCount((int)productPage.getTotalElements())
                .build();
    }

    @PostMapping
    ApiResponse addProduct(@RequestBody ProductRequest product){
        Product p = productService.createProduct(product);
        return ApiResponse.builder()
                .result(p.getId())
                .message("Thêm sản phẩm thaành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ListProductResponse>> getAllProducts() {
        return ApiResponse.<List<ListProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/top")
    public ApiResponse<List<ListProductResponse>> getTop5Products() {
        return ApiResponse.<List<ListProductResponse>>builder()
                .result(productService.getTop5())
                .build();
    }


    @GetMapping("/{id}")
    ApiResponse<ProductResponse> getProduct( @PathVariable String id){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProduct(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        return ApiResponse.builder()
                .message("Xóa sản pẩm thành công")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse updateProduct(@PathVariable String id, @RequestBody ProductResponse product){
        productService.updateProduct(id, product);
        return ApiResponse.builder()
                .message("Cập nhập sản phẩm thành công")
                .build();
    }

    @GetMapping("/stats")
    ApiResponse<Map<String, Object>> getProductStats(){
        return ApiResponse.<Map<String, Object>>builder()
                .result(productService.getProductStats())
                .build();
    }

    @GetMapping("/{productId}/can-review")
    public ApiResponse canReview(@PathVariable String productId,
                                             @RequestParam String userId) {
        boolean canReview = orderDetailRepository.existsByOrder_User_IdAndProduct_IdAndOrder_Status(userId, productId, OrderStatus.DELIVERED);
        boolean reviewed = reviewRepository.existsByUser_IdAndProduct_Id(userId, productId);
        boolean result = false;
        String msg = "";

        if (reviewed) {
            msg = "Bạn đã đánh giá sản phẩm này trước đó";
        } else if (!canReview) {
            msg = "Bạn phải mua sản phẩm mới có thể đánh giá";
        } else{
            result = true;
        }
        return ApiResponse.builder()
                .result(result)
                .message(msg)
                .build();
    }
}
