package com.pap.tech.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String status;
    BigDecimal totalamount;
    String vnpTxnref;
    Boolean paymentstatus;
    List<OrderDetailResponse> orderDetails;
    LocalDateTime orderdate;
    LocalDateTime shippeddate;
    LocalDateTime delivereddate;
    LocalDateTime cancelleddate;
    String paymentmethod;
    String shippingaddress;
    LocalDateTime vnpPaydate;
}
