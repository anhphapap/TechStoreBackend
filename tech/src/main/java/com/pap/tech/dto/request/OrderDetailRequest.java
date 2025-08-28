package com.pap.tech.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    Long price;
    int quantity;
    String productId;
    String orderId;
}
