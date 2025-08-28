package com.pap.tech.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    LocalDateTime orderdate;
    String status;
    String paymentmethod;
    Long totalAmount;
    String shippingaddress;
    String vnpTxnref;
    String userId;
    String addressId;
    List<OrderDetailRequest> orderDetails;
}
