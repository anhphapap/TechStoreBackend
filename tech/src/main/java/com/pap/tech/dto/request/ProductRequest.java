package com.pap.tech.dto.request;

import com.pap.tech.dto.response.ListProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    ListProductResponse product;
    String categoryId;
    String brandId;
    List<ImageRequest> productImages;
    List<AttributeRequest> productAttributes;

    @Data
    public static class ImageRequest {
        String url;
        String type;
    }

    @Data
    public static class AttributeRequest {
        String attributeId;
        String value;
    }
}
