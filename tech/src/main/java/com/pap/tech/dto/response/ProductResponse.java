package com.pap.tech.dto.response;

import com.pap.tech.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Product product;
    List<ProductImageResponse> productImages;
    List<ProductAttributeResponse> productAttributes;
}
