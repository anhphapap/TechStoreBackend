package com.pap.tech.dto.request;

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
public class OrderRequest {
    LocalDateTime orderdate;
    String status;
    String paymentmethod;
    BigDecimal totalamount;
    String shippingaddress;
    String vnpTxnref;
    String userId;
    String addressId;
    Boolean paymentstatus;
    BigDecimal discountamount;
    String discountcode;
    List<OrderDetailRequest> orderDetails;
}
